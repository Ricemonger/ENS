package app.utils.feign_clients.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityFeignClientService {

    private final SecurityFeignClient securityFeignClient;

    public String extractAccountId(String token) {
        return securityFeignClient.getAccountId(token);
    }

}
