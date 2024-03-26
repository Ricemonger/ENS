package app.utils.services.security.abstact.feign;

public class MockSecurityFeignClient implements SecurityFeignClient {
    @Override
    public String getAccountId(String securityToken) {
        return securityToken;
    }
}
