package app.utils;

public class MockSecurityJwtWebClient extends SecurityJwtWebClient {

    public MockSecurityJwtWebClient() {
    }

    public MockSecurityJwtWebClient(String string) {
    }

    @Override
    public String extractString(String securityToken, String uri) {
        return "9999";
    }
}
