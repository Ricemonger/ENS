package app.telegram.bot;

import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.clear.ClearCallback;
import app.telegram.bot.commands.clear.ClearDirect;
import app.telegram.bot.commands.contact.ContactDirect;
import app.telegram.bot.commands.contact.add.ContactAddCallback;
import app.telegram.bot.commands.contact.add.ContactAddChain;
import app.telegram.bot.commands.contact.add.ContactAddFinishCallback;
import app.telegram.bot.commands.contact.removeMany.ContactRemoveManyCallback;
import app.telegram.bot.commands.contact.removeMany.ContactRemoveManyChain;
import app.telegram.bot.commands.contact.removeMany.ContactRemoveManyFinishCallback;
import app.telegram.bot.commands.contact.removeOne.ContactRemoveOneCallback;
import app.telegram.bot.commands.contact.removeOne.ContactRemoveOneChain;
import app.telegram.bot.commands.contact.removeOne.ContactRemoveOneFinishCallback;
import app.telegram.bot.commands.data.DataDirect;
import app.telegram.bot.commands.data.DataRemoveCallback;
import app.telegram.bot.commands.data.DataShowCallback;
import app.telegram.bot.commands.help.HelpDirect;
import app.telegram.bot.commands.invalid.InvalidCallback;
import app.telegram.bot.commands.invalid.InvalidDirect;
import app.telegram.bot.commands.linking.LinkOrUnlinkDirect;
import app.telegram.bot.commands.linking.link.LinkCallback;
import app.telegram.bot.commands.linking.link.LinkChain;
import app.telegram.bot.commands.linking.link.LinkFinishCallback;
import app.telegram.bot.commands.linking.unlink.UnlinkCallback;
import app.telegram.bot.commands.notification.NotificationDirect;
import app.telegram.bot.commands.notification.add.NotificationAddCallback;
import app.telegram.bot.commands.notification.add.NotificationAddChain;
import app.telegram.bot.commands.notification.add.NotificationAddFinishCallback;
import app.telegram.bot.commands.notification.removeMany.NotificationRemoveManyCallback;
import app.telegram.bot.commands.notification.removeMany.NotificationRemoveManyChain;
import app.telegram.bot.commands.notification.removeMany.NotificationRemoveManyFinishCallback;
import app.telegram.bot.commands.notification.removeOne.NotificationRemoveOneCallback;
import app.telegram.bot.commands.notification.removeOne.NotificationRemoveOneChain;
import app.telegram.bot.commands.notification.removeOne.NotificationRemoveOneFinishCallback;
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
import app.telegram.users.model.InputGroup;
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
        log.info("onUpdateReceived received update-{}", update);
        if (update.hasMessage()) {
            InputGroup inputGroup = botService.geUserInputGroup(update.getMessage().getChatId());
            log.info("update is-{}, user's inputGroup-{}", update, inputGroup);
            if (inputGroup == InputGroup.BASE) {
                listenCommandAndExecute(update);
            } else {
                listenUserInputAndExecute(inputGroup, update);
            }
        } else if (update.hasCallbackQuery()) {
            listenQueryAndExecute(update);
        }
    }

    private void listenCommandAndExecute(Update update) {
        log.info("listenCommandAndExecute was called for update-{}", update);
        switch (update.getMessage().getText()) {
            case "/start" -> new StartDirect(this, update, botService).execute();
            case "/send" -> new SendDirect(this, update, botService).execute();
            case "/sendall" -> new SendAllDirect(this, update, botService).execute();
            case "/help" -> new HelpDirect(this, update, botService).execute();
            case "/contact" -> new ContactDirect(this, update, botService).execute();
            case "/notification" -> new NotificationDirect(this, update, botService).execute();
            case "/clear" -> new ClearDirect(this, update, botService).execute();
            case "/link" -> new LinkOrUnlinkDirect(this, update, botService).execute();
            case "/data" -> new DataDirect(this, update, botService).execute();
            default -> new InvalidDirect(this, update, botService).execute();
        }
    }

    private void listenQueryAndExecute(Update update) {
        log.info("listenQueryAndExecute was called for update-{}", update);
        switch (update.getCallbackQuery().getData()) {
            case Callbacks.REGISTER_YES -> new RegisterYesCallback(this, update, botService).execute();
            case Callbacks.REGISTER_NO -> new RegisterNoCallback(this, update, botService).execute();

            case Callbacks.SEND_ONE -> new SendOneCommand(this, update, botService).execute();
            case Callbacks.SEND_MANY -> new SendManyCommand(this, update, botService).execute();

            case Callbacks.SEND_ALL -> new SendAllCallback(this, update, botService).execute();

            case Callbacks.CONTACT_ADD -> new ContactAddCallback(this, update, botService).execute();
            case Callbacks.CONTACT_ADD_FINISH -> new ContactAddFinishCallback(this, update, botService).execute();

            case Callbacks.CONTACT_REMOVE_ONE -> new ContactRemoveOneCallback(this, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_ONE_FINISH ->
                    new ContactRemoveOneFinishCallback(this, update, botService).execute();

            case Callbacks.CONTACT_REMOVE_MANY -> new ContactRemoveManyCallback(this, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_MANY_FINISH ->
                    new ContactRemoveManyFinishCallback(this, update, botService).execute();

            case Callbacks.NOTIFICATION_ADD -> new NotificationAddCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_ADD_FINISH ->
                    new NotificationAddFinishCallback(this, update, botService).execute();

            case Callbacks.NOTIFICATION_REMOVE_ONE ->
                    new NotificationRemoveOneCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_ONE_FINISH -> new NotificationRemoveOneFinishCallback(this, update,
                    botService).execute();

            case Callbacks.NOTIFICATION_REMOVE_MANY ->
                    new NotificationRemoveManyCallback(this, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_MANY_FINISH -> new NotificationRemoveManyFinishCallback(this, update,
                    botService).execute();


            case Callbacks.DATA_SHOW -> new DataShowCallback(this, update, botService).execute();
            case Callbacks.DATA_REMOVE -> new DataRemoveCallback(this, update, botService).execute();

            case Callbacks.CLEAR -> new ClearCallback(this, update, botService).execute();

            case Callbacks.LINK -> new LinkCallback(this, update, botService).execute();
            case Callbacks.LINK_FINISH -> new LinkFinishCallback(this, update, botService).execute();

            case Callbacks.UNLINK -> new UnlinkCallback(this, update, botService).execute();

            case Callbacks.CANCEL -> new CancelCallback(this, update, botService).execute();

            default -> new InvalidCallback(this, update, botService).execute();
        }
    }

    private void listenUserInputAndExecute(InputGroup inputGroup, Update update) {
        log.info("listenUserInputAndExecute was called for update-{} and inputGroup-{}", update, inputGroup);
        switch (inputGroup) {
            case CONTACT_ADD_ONE -> new ContactAddChain(this, update, botService).execute();
            case CONTACT_REMOVE_ONE -> new ContactRemoveOneChain(this, update, botService).execute();
            case CONTACT_REMOVE_MANY -> new ContactRemoveManyChain(this, update, botService).execute();

            case NOTIFICATION_ADD_ONE -> new NotificationAddChain(this, update, botService).execute();
            case NOTIFICATION_REMOVE_ONE -> new NotificationRemoveOneChain(this, update, botService).execute();
            case NOTIFICATION_REMOVE_MANY -> new NotificationRemoveManyChain(this, update, botService).execute();

            case LINK -> new LinkChain(this, update, botService).execute();
        }
    }
}

