package app.utils.feign_clients;

public record ChangeAccountIdRequest(
        String newAccountIdToken
) {
}
