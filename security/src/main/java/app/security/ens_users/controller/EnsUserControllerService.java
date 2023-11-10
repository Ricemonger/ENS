package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.model.EnsUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnsUserControllerService {

    private final EnsUserService ensUserService;

    public String register(EnsUserRegisterRequest request) {
        String token = ensUserService.register(request.toUser());
        log.trace("register method was called with request-{} and result-{}", request, token);
        return token;
    }

    public String login(EnsUserLoginRequest request) {
        String token = ensUserService.login(request.toUser());
        log.trace("login method was called with request-{} and result-{}", request, token);
        return token;
    }
}
