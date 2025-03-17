package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.AccountLoginRequest;
import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.request.RefreshTokenRequest;
import cgv_cinemas_ticket.demo.dto.request.VerifyEmailRequest;
import cgv_cinemas_ticket.demo.dto.response.AccountResponse;
import cgv_cinemas_ticket.demo.dto.response.AuthenticationResponse;
import cgv_cinemas_ticket.demo.dto.response.RefreshTokenResponse;
import cgv_cinemas_ticket.demo.dto.response.SentEmailVerifyResponse;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientRoleResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.exception.ErrorCode;
import cgv_cinemas_ticket.demo.mapper.IAccountMapper;
import cgv_cinemas_ticket.demo.mapper.IRoleMapper;
import cgv_cinemas_ticket.demo.mapper.IUserMapper;
import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.model.Level;
import cgv_cinemas_ticket.demo.model.Role;
import cgv_cinemas_ticket.demo.model.User;
import cgv_cinemas_ticket.demo.repository.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    IAccountRepository accountRepository;
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    IUserRepository userRepository;
    ILevelRepository levelRepository;

    IUserMapper userMapper;
    IAccountMapper accountMapper;
    IRoleMapper roleMapper;

    PasswordEncoder passwordEncoder;
    JavaMailSender mailSender;

    @NonFinal
    @Value("${jwt.secret-key}")
    String secretKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    Long validDuration;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    Long refreshDuration;

    @NonFinal
    @Value("${domain-client}")
    String domainClient;

    public AccountResponse handleSignupAccountClient(AccountSignupRequest accountSignupRequest) throws AppException {
        MessageResponse messageResponse = MessageResponse.SIGNUP_ACCOUNT_FAILED;
        Role role = roleRepository.findById(3L).orElseThrow(
                () -> new AppException(messageResponse.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
        Level level = levelRepository.findById(1l).orElseThrow(
                () -> new AppException(messageResponse.getMessage(), HttpStatus.BAD_REQUEST.value()
                ));
        User user = userMapper.toAccountSigntoUser(accountSignupRequest);
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        userRepository.save(user);

        Account account = accountMapper.toAccountSignupToAccount(accountSignupRequest);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setStatus(true);
        account.setRoles(roles);
        account.setLevel(level);
        account.setUser(user);
        account.setActive(false);
        account.setCreateAt(new Date());
        account.setUpdateAt(new Date());

        // hash password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        AccountResponse accountResponse = accountMapper.toAccountToAccountResponse(account);
        Set<ClientRoleResponse> rolesResponse = account.getRoles().stream()
                .map(roleMapper::toRoleToClientRoleResponse)
                .collect(Collectors.toSet());
        accountResponse.setRoles(rolesResponse);
        return accountResponse;
    }

    public AuthenticationResponse handleAuthentication(AccountLoginRequest accountLoginRequest) throws AppException {
        Account account = accountRepository.findByEmailOrPhoneNumber(accountLoginRequest.getAccount(), accountLoginRequest.getAccount());
        if (account == null) {
            ErrorCode errorCode = ErrorCode.ACCOUNT_NOT_EXITED;
            throw new AppException(errorCode.getMessage(), errorCode.getStatusCode().value());
        }

        if (!passwordEncoder.matches(accountLoginRequest.getPassword(), account.getPassword())) {
            ErrorCode errorCode = ErrorCode.PASSWORD_NOT_MATCH;
            throw new AppException(errorCode.getMessage(), errorCode.getStatusCode().value());
        }
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(generateJWTToken(account, validDuration));
        authenticationResponse.setRefreshToken(generateJWTToken(account, refreshDuration));
        authenticationResponse.setAccount(accountMapper.toAccountToAccountResponse(account));
        return authenticationResponse;
    }

    public void handleLogout(HttpServletRequest request) throws ParseException {
    }

    public AuthenticationResponse handleRefreshToken(HttpServletRequest request) throws ParseException {
        String refreshToken = request.getHeader("Authorization").split(" ")[1];
        SignedJWT signedJWT = verifyToken(refreshToken, true);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        String email = jwtClaimsSet.getSubject();
        Account account = accountRepository.findByEmail(email);
        String newAccessToken = generateJWTToken(account, validDuration);
        String newRefreshToken = generateJWTToken(account, refreshDuration);
        return AuthenticationResponse
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public String generateJWTToken(Account account, Long expirationTime) throws RuntimeException {
        long now = System.currentTimeMillis();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer("CGV Movies Ticket")
                .issueTime(new Date())
                .expirationTime(new Date(
                        now + (expirationTime * 1000)
                ))
                .claim("scope", buildScopeClaim(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (Exception ex) {
            log.error("Cannot create token", ex);
            throw new RuntimeException(ex);
        }
    }

    public String generateJWTTokenForVerifyEmail(String email) throws RuntimeException {
        long now = System.currentTimeMillis();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("CGV Movies Ticket")
                .issueTime(new Date())
                .expirationTime(new Date(
                        now + (60 * 1000)
                ))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (Exception ex) {
            log.error("Cannot create token", ex);
            throw new RuntimeException(ex);
        }
    }

    public SentEmailVerifyResponse handleSentEmailVerify(String email) throws MessagingException {
        MessageResponse messageResponse = MessageResponse.SENT_EMAIL_VERIFY_ACCOUNT_SUCCESS;
        String tokenVerifyEmail = generateJWTTokenForVerifyEmail(email);
        sentEmailToVerify(email, tokenVerifyEmail);
        return SentEmailVerifyResponse.builder()
                .status(true)
                .message(messageResponse.getMessage())
                .build();
    }

    public boolean handleVerifyEmail(String token, VerifyEmailRequest verifyEmailRequest) {
        try {
            SignedJWT signedJWT = verifyToken(token, false);
            String emailDecodedJWT = signedJWT.getJWTClaimsSet().getSubject();
            if(emailDecodedJWT.equals(verifyEmailRequest.getEmail())){
                Account account = accountRepository.findByEmail(emailDecodedJWT);
                account.setActive(true);
                accountRepository.save(account);
                return true;
            }
           return false;
        } catch (Exception e) {
            ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
            throw new JwtException(errorCode.getMessage());
        }
    }

    public SignedJWT verifyToken(String jwtToken, boolean isRefreshToken) {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
        try {

            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            Date expirationTime = jwtClaimsSet.getExpirationTime();
            var verified = signedJWT.verify(verifier);
            if (isRefreshToken && !expirationTime.after(new Date())) {
                errorCode = ErrorCode.REFRESH_TOKEN_INVALID;
                throw new JwtException(errorCode.getMessage());
            } else if (!(verified && expirationTime.after(new Date()))) {
                throw new JwtException(errorCode.getMessage());
            }
            return signedJWT;
        } catch (Exception ex) {
            throw new JwtException(errorCode.getMessage());
        }
    }

    public void sentEmailToVerify(String toEmail, String tokenVerifyEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("CGV Movies Ticket - Xác nhận email tài khoản của bạn!");

        // Nội dung HTML
        String htmlContent = String.format("""
                    <html>
                    <body>
                        <h2 style="color: blue;">Chào mừng bạn đến với CGV Movies Ticket!</h2>
                        <p>Click vào link dưới đây để xác nhận email tài khoản của bạn:</p>
                        <a href="%s/user/verify-email/%s">Xác nhận ngay</a>
                        <br>
                        <p style="font-size: 12px; color: gray;">Nếu bạn không yêu cầu đăng ký, hãy bỏ qua email này.</p>
                    </body>
                    </html>
                """,domainClient, tokenVerifyEmail);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public static String buildScopeClaim(Account account) {
        StringBuilder scopeBuilder = new StringBuilder();
        account.getRoles().forEach(role -> {
            String formatedRoleName = role.getName().replace(" ", "_").toUpperCase();
            scopeBuilder.append("ROLE_").append(formatedRoleName);
            role.getPermissions().forEach(permission -> {
                String formatedPermissionName = permission.getName().replace(" ", "_").toUpperCase();
                scopeBuilder.append(" ").append(formatedPermissionName);
            });
        });
        return scopeBuilder.toString();
    }
}
