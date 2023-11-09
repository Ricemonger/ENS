package app.security.abstract_users.controller;

import app.security.abstract_users.security.AbstractUserJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AbstractUserControllerService {

    private final AbstractUserJwtUtil abstractUserJwtUtil;

    public String getAccountId(String securityToken) {
        log.trace("getAccountId method was called with securityToken-{}", securityToken);
        String result = abstractUserJwtUtil.extractAccountId(securityToken);
        log.trace("getAccountId method's with securityToken-{} result is {}", securityToken, result);
        return result;
    }
}
