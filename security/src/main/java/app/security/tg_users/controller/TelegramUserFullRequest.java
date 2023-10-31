package app.security.tg_users.controller;

public record TelegramUserFullRequest(
        String accountId,
        String chatId
) {
}
