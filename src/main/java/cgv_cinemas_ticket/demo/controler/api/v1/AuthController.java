package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.constraint.MessageResponse;
import cgv_cinemas_ticket.demo.dto.request.AccountLoginRequest;
import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.request.VerifyEmailRequest;
import cgv_cinemas_ticket.demo.dto.response.*;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthController {
    AuthService authServices;

    @NonFinal
    @Value("${jwt.valid-duration}")
    Long validDuration;

    @PostMapping("/signup")
    ResponseEntity<ApiResponse<AccountResponse>> signupAccountClient(@RequestBody @Valid AccountSignupRequest requestBody) throws AppException {
        MessageResponse messageResponse = MessageResponse.SIGNUP_ACCOUNT_SUCCESS;
        AccountResponse clientAccountResponse = authServices.handleSignupAccountClient(requestBody);
        return ResponseEntity.ok(ApiResponse.<AccountResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(clientAccountResponse).build());
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authentication(HttpServletResponse response, @RequestBody AccountLoginRequest requestBody) throws AppException {
        MessageResponse messageResponse = MessageResponse.AUTHENTICATION_SUCCESS;
        AuthenticationResponse authenticationResponse = authServices.handleAuthentication(requestBody);
        Cookie cookie = new Cookie("accessToken", authenticationResponse.getAccessToken()); // Tạo cookie với tên và giá trị
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(validDuration)); // Cookie exist in 30day = seconds
        response.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(authenticationResponse)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request) throws ParseException {
        MessageResponse messageResponse = MessageResponse.LOGOUT_SUCCESS;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "");

        authServices.handleLogout(request);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(
                        ApiResponse.builder()
                                .status(true)
                                .statusCode(200)
                                .message(messageResponse.getMessage())
                                .build()
                );
    }

    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        MessageResponse messageResponse = MessageResponse.REFRESH_TOKEN_SUCCESS;
        AuthenticationResponse authenticationResponse = authServices.handleRefreshToken(request);
        Cookie cookie = new Cookie("accessToken", authenticationResponse.getAccessToken()); // Tạo cookie với tên và giá trị
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(validDuration)); // Cookie exist in 30day = seconds
        response.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(authenticationResponse)
                .build());
    }

    @GetMapping("/sent-email-verify")
    ResponseEntity<ApiResponse<SentEmailVerifyResponse>> sentEmailVerify(@RequestParam String email) throws AppException, MessagingException {
        MessageResponse messageResponse = MessageResponse.SENT_EMAIL_VERIFY_ACCOUNT_SUCCESS;
        return ResponseEntity.ok(ApiResponse.<SentEmailVerifyResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .data(authServices.handleSentEmailVerify(email))
                .build());
    }

    @PostMapping("/verify-email")
    ResponseEntity<ApiResponse<Object>> verifyEmail(@RequestParam String token, @RequestBody VerifyEmailRequest verifyEmailRequest) {
        MessageResponse messageResponse = MessageResponse.VERIFY_EMAIL_ACCOUNT_SUCCESS;
        return ResponseEntity.ok(ApiResponse.builder()
                .status(authServices.handleVerifyEmail(token, verifyEmailRequest))
                .statusCode(HttpStatus.OK.value())
                .message(messageResponse.getMessage())
                .build());
    }
}
