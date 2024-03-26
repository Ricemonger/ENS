package app.security.abstract_users.controller;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbstractUserControllerService {

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public String getAccountId(String securityToken) {
        return abstractUserJwtUtil.extractAccountId(securityToken);
    }
}
