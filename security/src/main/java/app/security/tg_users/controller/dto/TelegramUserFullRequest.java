package app.security.tg_users.controller.dto;

public record TelegramUserFullRequest(
        String accountId,
        String chatId
) {
}
