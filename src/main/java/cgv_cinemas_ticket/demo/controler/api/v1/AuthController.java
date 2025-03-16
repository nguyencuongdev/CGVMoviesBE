package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.ApiResponse;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientAccountResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.service.AuthServices;
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
    AuthServices authServices;

    @PostMapping("/signup")
    ResponseEntity<ApiResponse<ClientAccountResponse>> signupAccountClient(@RequestBody AccountSignupRequest requestBody) throws AppException {
        ClientAccountResponse clientAccountResponse = authServices.handleSignupAccountClient(requestBody);
        return ResponseEntity.ok(ApiResponse.<ClientAccountResponse>builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Signup account successful!")
                .data(clientAccountResponse).build());
    }
}
