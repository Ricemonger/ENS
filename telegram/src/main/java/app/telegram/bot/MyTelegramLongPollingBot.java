package app.telegram.bot;

import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.clear.ClearCallback;
import app.telegram.bot.commands.clear.ClearDirect;
import app.telegram.bot.commands.contact.*;
import app.telegram.bot.commands.data.DataDirect;
import app.telegram.bot.commands.data.DataRemoveCallback;
import app.telegram.bot.commands.data.DataShowCallback;
import app.telegram.bot.commands.help.HelpDirect;
import app.telegram.bot.commands.invalid.InvalidCallback;
import app.telegram.bot.commands.invalid.InvalidDirect;
import app.telegram.bot.commands.link.LinkCallback;
import app.telegram.bot.commands.link.LinkDirect;
import app.telegram.bot.commands.link.UnlinkCallback;
import app.telegram.bot.commands.notification.*;
import app.telegram.bot.commands.send.SendDirect;
import app.telegram.bot.commands.send.SendManyCommand;
import app.telegram.bot.commands.send.SendOneCommand;
import app.telegram.bot.commands.sendall.SendAllCallback;
import app.telegram.bot.commands.sendall.SendAllDirect;
import app.telegram.bot.commands.start.RegisterNoCallback;
import app.telegram.bot.commands.start.RegisterYesCallback;
import app.telegram.bot.commands.start.StartDirect;
import app.telegram.bot.config.BotCommandsConfig;
import app.telegram.bot.config.TelegramBotAuthorizationConfiguration;
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
            , BotCommandsConfig config
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
            case "/start" -> new StartDirect(this, update, botService).execute();
            case "/send" -> new SendDirect(this, update, botService).execute();
            case "/sendall" -> new SendAllDirect(this, update, botService).execute();
            case "/help" -> new HelpDirect(this, update, botService).execute();
            case "/contact" -> new ContactDirect(this, update, botService).execute();
            case "/notification" -> new NotificationDirect(this, update, botService).execute();
            case "/clear" -> new ClearDirect(this, update, botService).execute();
            case "/link" -> new LinkDirect(this, update, botService).execute();
            case "/data" -> new DataDirect(this, update, botService).execute();
            default -> new InvalidDirect(this, update, botService).execute();
        }
    }

    public void listenQueryAndExecute(Update update) {
        switch (update.getCallbackQuery().getData()) {
            case Callbacks.REGISTER_YES -> new RegisterYesCallback(this, update, botService).execute();
            case Callbacks.REGISTER_NO -> new RegisterNoCallback(this, update, botService).execute();

            case Callbacks.SEND_ONE -> new SendOneCommand(this, update, botService).execute();
            case Callbacks.SEND_MANY -> new SendManyCommand(this, update, botService).execute();

            case Callbacks.SEND_ALL -> new SendAllCallback(this, update, botService).execute();

            case Callbacks.CONTACT_ADD_ONE -> new ContactAddOneCallback(this, update, botService).execute();
            case Callbacks.CONTACT_ADD_MANY -> new ContactAddManyCallback(this, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_ONE -> new ContactRemoveOneCallback(this, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_MANY -> new ContactRemoveManyCallback(this, update, botService).execute();

            case Callbacks.NOTIFICATION_ADD_ONE -> new NotificationAddOneCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_ADD_MANY -> new NotificationAddManyCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_ONE ->
                    new NotificationRemoveOneCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_MANY ->
                    new NotificationRemoveManyCallback(this, update, botService).execute();

            case Callbacks.DATA_SHOW -> new DataShowCallback(this, update, botService).execute();
            case Callbacks.DATA_REMOVE -> new DataRemoveCallback(this, update, botService).execute();

            case Callbacks.CLEAR -> new ClearCallback(this, update, botService).execute();

            case Callbacks.LINK -> new LinkCallback(this, update, botService).execute();
            case Callbacks.UNLINK -> new UnlinkCallback(this, update, botService).execute();

            case Callbacks.CANCEL -> new CancelCallback(this, update, botService).execute();

            default -> new InvalidCallback(this, update, botService).execute();
        }
    }
}

