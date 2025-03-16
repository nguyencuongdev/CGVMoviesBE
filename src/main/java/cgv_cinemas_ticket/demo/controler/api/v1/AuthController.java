package cgv_cinemas_ticket.demo.controler.api.v1;

import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.service.AuthServices;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthController {
    AuthServices authServices;
    @GetMapping("")
    ResponseEntity<List<Account>> getAccountList(){
        return  ResponseEntity.ok(authServices.getAccounts());
    }
}
