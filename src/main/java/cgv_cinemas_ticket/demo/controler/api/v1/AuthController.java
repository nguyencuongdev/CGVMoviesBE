package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.dto.request.AccountLoginRequest;
import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.request.RefreshTokenRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.AccountResponse;
import cgv_cinemas_ticket.demo.dto.response.AuthenticationResponse;
import cgv_cinemas_ticket.demo.dto.response.RefreshTokenResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.persistence.GeneratedValue;
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
        AccountResponse clientAccountResponse = authServices.handleSignupAccountClient(requestBody);
        return ResponseEntity.ok(ApiResponse.<AccountResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Signup account successful!")
                .data(clientAccountResponse).build());
    }

    @PostMapping("/login")

    ResponseEntity<ApiResponse<AuthenticationResponse>> authentication(HttpServletResponse response, @RequestBody AccountLoginRequest requestBody) throws AppException {
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
                .message("Authentication successful!")
                .data(authenticationResponse)
                .build());
    }

    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(HttpServletRequest request,HttpServletResponse response) throws ParseException, JOSEException {
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
                .message("Refresh token successful!")
                .data(authenticationResponse)
                .build());
    }
}
