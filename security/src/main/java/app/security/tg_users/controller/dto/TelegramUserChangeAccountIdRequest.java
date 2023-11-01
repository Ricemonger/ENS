package app.security.tg_users.controller.dto;

public record TelegramUserChangeAccountIdRequest(
        String oldAccountId,
        String newAccountId
) {
}
