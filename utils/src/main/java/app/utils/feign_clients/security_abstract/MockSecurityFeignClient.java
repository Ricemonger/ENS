package app.utils.feign_clients.security_abstract;

public class MockSecurityFeignClient implements SecurityFeignClient {
    @Override
    public String getAccountId(String securityToken) {
        return securityToken;
    }
}
