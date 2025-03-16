package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientAccountResponse;
import cgv_cinemas_ticket.demo.dto.response.end_user.ClientRoleResponse;
import cgv_cinemas_ticket.demo.exception.AppException;
import cgv_cinemas_ticket.demo.mapper.IAccountMapper;
import cgv_cinemas_ticket.demo.mapper.IRoleMapper;
import cgv_cinemas_ticket.demo.mapper.IUserMapper;
import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.model.Level;
import cgv_cinemas_ticket.demo.model.Role;
import cgv_cinemas_ticket.demo.model.User;
import cgv_cinemas_ticket.demo.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServices {
    IAccountRepository accountRepository;
    ;
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    IUserRepository userRepository;
    ILevelRepository levelRepository;

    IUserMapper userMapper;
    IAccountMapper accountMapper;
    IRoleMapper roleMapper;

    PasswordEncoder passwordEncoder;

    public ClientAccountResponse handleSignupAccountClient(AccountSignupRequest accountSignupRequest) throws AppException {
        Role role = roleRepository.findById(3L).orElseThrow(
                () -> new AppException("Signup acccount failed!", HttpStatus.BAD_REQUEST.value()
                ));
        Level level = levelRepository.findById(1l).orElseThrow(
                () -> new AppException("Signup acccount failed!", HttpStatus.BAD_REQUEST.value()
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
        account.setCreateAt(new Date());
        account.setUpdateAt(new Date());

        // hash password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        ClientAccountResponse clientAccountResponse = accountMapper.toAccountToClientAccountResponse(account);
        Set<ClientRoleResponse> rolesResponse = account.getRoles().stream()
                .map(roleMapper::toRoleToClientRoleResponse)
                .collect(Collectors.toSet());
        clientAccountResponse.setRoles(rolesResponse);
        return clientAccountResponse;
    }
}
