package app.telegram.mytgbot;

import app.telegram.mytgbot.commands.callbackCommand.*;
import app.telegram.mytgbot.commands.directCommand.*;
import app.telegram.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class MyTelegramLongPollingBot extends TelegramLongPollingBot {

    private final TelegramBotAuthorizationConfiguration authConfig;

    private final BotService botService;

    @Autowired
    public MyTelegramLongPollingBot(TelegramBotAuthorizationConfiguration authConfig
            , TelegramBotCommandsConfiguration config
            , BotService botService) {
        super(authConfig.getAPI_TOKEN());
        this.authConfig = authConfig;
        this.botService = botService;
        try {
            this.execute(new SetMyCommands(config.getPublicCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return authConfig.getBOT_NAME();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            listenCommandAndExecute(update);
        } else if (update.hasCallbackQuery()) {
            listenQueryAndExecute(update);
        }
    }

    public void listenCommandAndExecute(Update update) {
        switch (update.getMessage().getText()) {
            case "/start" -> new StartBotCommand(this, update, botService).execute();
            case "/send" -> new SendBotCommand(this, update, botService).execute();
            case "/sendall" -> new SendAllBotCommand(this, update, botService).execute();
            case "/help" -> new HelpBotCommand(this, update, botService).execute();
            case "/contact" -> new ContactBotCommand(this, update, botService).execute();
            case "/notification" -> new NotificationBotCommand(this, update, botService).execute();
            case "/clear" -> new ClearBotCommand(this, update, botService).execute();
            case "/link" -> new LinkBotCommand(this, update, botService).execute();
            case "/data" -> new DataBotCommand(this, update, botService).execute();
            default -> new InvalidBotCommand(this, update, botService).execute();
        }
    }

    public void listenQueryAndExecute(Update update) {
        switch (update.getCallbackQuery().getData()) {
            case "REGISTER_YES" -> new RegisterYesBotCommand(this, update, botService).execute();
            case "REGISTER_NO" -> new RegisterNoBotCommand(this, update, botService).execute();
            case "SEND_ALL_YES" -> new SendAllYesBotCommand(this, update, botService).execute();
            case "CLEAR_YES" -> new ClearYesBotCommand(this, update, botService).execute();
            case "SEND_ALL_NO", "CLEAR_NO" -> new CancelBotCommand(this, update, botService).execute();
            default -> new InvalidCallbackBotCommand(this, update, botService).execute();
        }
    }
}

