package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.dto.request.AccountLoginRequest;
import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.AccountResponse;
import cgv_cinemas_ticket.demo.dto.response.AuthenticationResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthController {
    AuthService authServices;

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
    ResponseEntity<ApiResponse<AuthenticationResponse>> authentication(@RequestBody AccountLoginRequest requestBody) throws AppException {
        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Authentication successful!")
                .data(authServices.handleAuthentication(requestBody))
                .build());
    }
}
