package app.telegram.bot.api;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.BotCommandsConfig;
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
import app.telegram.bot.commands.data.remove.DataRemoveCallback;
import app.telegram.bot.commands.data.remove.DataRemoveCallbackFinish;
import app.telegram.bot.commands.data.show.DataShowCallback;
import app.telegram.bot.commands.errors.InternalError;
import app.telegram.bot.commands.help.HelpDirect;
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
import app.telegram.bot.commands.send.sendMany.SendManyCallback;
import app.telegram.bot.commands.send.sendMany.SendManyChain;
import app.telegram.bot.commands.send.sendMany.SendManyFinishCallback;
import app.telegram.bot.commands.send.sendOne.SendOneCallback;
import app.telegram.bot.commands.send.sendOne.SendOneChain;
import app.telegram.bot.commands.send.sendOne.SendOneFinishCallback;
import app.telegram.bot.commands.sendall.SendAllCallback;
import app.telegram.bot.commands.sendall.SendAllDirect;
import app.telegram.bot.commands.start.RegisterNoCallback;
import app.telegram.bot.commands.start.RegisterYesCallback;
import app.telegram.bot.commands.start.StartDirect;
import app.telegram.bot.exceptions.internal.InternalErrorException;
import app.telegram.bot.exceptions.internal.InvalidCallbackException;
import app.telegram.bot.exceptions.internal.InvalidUserInputGroupException;
import app.telegram.bot.exceptions.internal.ListeningMethodCouldNotBeChosenException;
import app.telegram.bot.exceptions.user.InvalidDirectCommandException;
import app.telegram.users.model.InputGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
        log.info("onUpdateReceived received update-{}", update);

        Long chatId = null;
        CallbackQuery query = null;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            query = update.getCallbackQuery();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        InputGroup inputGroup = botService.getNextInputGroupOrBase(chatId);
        log.info("User's input group is-{} for update-{}", inputGroup, update.getUpdateId());

        if (update.hasMessage() && inputGroup == InputGroup.BASE) {
            listenDirectCommandAndExecute(update);
        } else if (update.hasMessage() || (query != null && (query.getData().equals(Callbacks.EMPTY) || Callbacks.isMethod(query.getData())))) {
            listenUserInputAndExecute(inputGroup, update);
        } else if (query != null) {
            listenCallbackQueryAndExecute(update);
        } else {
            log.error("No listening method chosen for update-{}", update.getUpdateId());
            throw new ListeningMethodCouldNotBeChosenException(update);
        }
    }

    private void listenDirectCommandAndExecute(Update update) {
        log.info("listenCommandAndExecute was called for update-{}", update.getUpdateId());
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
            default -> throw new InvalidDirectCommandException(update);
        }
    }

    private void listenCallbackQueryAndExecute(Update update) {
        log.info("listenQueryAndExecute was called for update-{}", update.getUpdateId());
        switch (update.getCallbackQuery().getData()) {
            case Callbacks.REGISTER_YES -> new RegisterYesCallback(this, update, botService).execute();
            case Callbacks.REGISTER_NO -> new RegisterNoCallback(this, update, botService).execute();

            case Callbacks.SEND_ONE -> new SendOneCallback(this, update, botService).execute();
            case Callbacks.SEND_ONE_FINISH -> new SendOneFinishCallback(this, update, botService).execute();

            case Callbacks.SEND_MANY -> new SendManyCallback(this, update, botService).execute();
            case Callbacks.SEND_MANY_FINISH -> new SendManyFinishCallback(this, update, botService).execute();

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
            case Callbacks.DATA_REMOVE_FINISH -> new DataRemoveCallbackFinish(this, update, botService).execute();

            case Callbacks.CLEAR -> new ClearCallback(this, update, botService).execute();

            case Callbacks.LINK -> new LinkCallback(this, update, botService).execute();
            case Callbacks.LINK_FINISH -> new LinkFinishCallback(this, update, botService).execute();

            case Callbacks.UNLINK -> new UnlinkCallback(this, update, botService).execute();

            case Callbacks.CANCEL -> new CancelCallback(this, update, botService).execute();

            default -> throw new InvalidCallbackException(update);
        }
    }

    private void listenUserInputAndExecute(InputGroup inputGroup, Update update) {
        log.info("listenUserInputAndExecute was called for update-{} and inputGroup-{}", update.getUpdateId(), inputGroup);
        switch (inputGroup) {
            case SEND_ONE -> new SendOneChain(this, update, botService).execute();
            case SEND_MANY -> new SendManyChain(this, update, botService).execute();

            case CONTACT_ADD_ONE -> new ContactAddChain(this, update, botService).execute();
            case CONTACT_REMOVE_ONE -> new ContactRemoveOneChain(this, update, botService).execute();
            case CONTACT_REMOVE_MANY -> new ContactRemoveManyChain(this, update, botService).execute();

            case NOTIFICATION_ADD_ONE -> new NotificationAddChain(this, update, botService).execute();
            case NOTIFICATION_REMOVE_ONE -> new NotificationRemoveOneChain(this, update, botService).execute();
            case NOTIFICATION_REMOVE_MANY -> new NotificationRemoveManyChain(this, update, botService).execute();

            case LINK -> new LinkChain(this, update, botService).execute();

            default -> throw new InvalidUserInputGroupException(update);
        }
    }


    @ExceptionHandler(InternalErrorException.class)
    public void internalTelegramError(InternalErrorException e) {
        new InternalError(this, e.getUpdate(), botService).execute();
    }

    @ExceptionHandler(Exception.class)
    public void internalTelegramError(Exception e) {

    }
}

