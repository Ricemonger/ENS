package app.security.ens_users.controller;

import app.security.ens_users.controller.dto.EnsUserLoginRequest;
import app.security.ens_users.controller.dto.EnsUserRegisterRequest;
import app.security.ens_users.model.EnsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnsUserControllerService {

    private final EnsUserService ensUserService;

    public String register(EnsUserRegisterRequest request) {
        return ensUserService.register(request.toUser());
    }

    public String login(EnsUserLoginRequest request) {
        return ensUserService.login(request.toUser());
    }
}
