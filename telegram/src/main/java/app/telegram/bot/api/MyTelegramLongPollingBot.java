package app.telegram.bot.api;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.BotCommandsConfig;
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
    public MyTelegramLongPollingBot(
            TelegramBotAuthorizationConfiguration authConfig,
            BotCommandsConfig config,
            BotService botService) {
        super(authConfig.getAPI_TOKEN());
        this.authConfig = authConfig;
        this.botService = botService;
        log.info("MyTelegramLongPollingBot was initialized with Api Token-{}, Username-{}", authConfig.getAPI_TOKEN(),
                authConfig.getBOT_NAME());
        try {
            this.execute(new SetMyCommands(config.getPublicCommands(), new BotCommandScopeDefault(), null));
            botService.setBaseInputAndGroupForAllUsers();
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
        new UpdateReceiverAndExecutor(this, update, botService);
    }
}

