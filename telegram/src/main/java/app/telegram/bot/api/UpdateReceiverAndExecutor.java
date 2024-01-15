package app.telegram.bot.api;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
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
import app.telegram.bot.commands.errors.UnknownError;
import app.telegram.bot.commands.errors.*;
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
import app.telegram.bot.commands.settings.SettingsDirect;
import app.telegram.bot.commands.settings.actionConfirmation.ActionConfirmationCallback;
import app.telegram.bot.commands.settings.actionConfirmation.ActionConfirmationDisableCallback;
import app.telegram.bot.commands.settings.actionConfirmation.ActionConfirmationEnableCallback;
import app.telegram.bot.commands.settings.customPhrase.CustomPhraseCallback;
import app.telegram.bot.commands.settings.customPhrase.CustomPhraseChain;
import app.telegram.bot.commands.settings.customPhrase.CustomPhraseFinishCallback;
import app.telegram.bot.commands.start.RegisterNoCallback;
import app.telegram.bot.commands.start.RegisterYesCallback;
import app.telegram.bot.commands.start.StartDirect;
import app.telegram.bot.commands.task.TaskDirect;
import app.telegram.bot.commands.task.create.TaskCreateCallback;
import app.telegram.bot.commands.task.create.TaskCreateChain;
import app.telegram.bot.commands.task.create.TaskCreateFinishCallback;
import app.telegram.bot.commands.task.delete.TaskDeleteCallback;
import app.telegram.bot.commands.task.delete.TaskDeleteChain;
import app.telegram.bot.commands.task.delete.TaskDeleteFinishCallback;
import app.telegram.bot.commands.task.deleteAll.TaskDeleteAllCallback;
import app.telegram.bot.commands.task.deleteAll.TaskDeleteAllFinishCallback;
import app.telegram.bot.commands.task.show.TaskShowCallback;
import app.telegram.bot.exceptions.internal.InternalErrorException;
import app.telegram.bot.exceptions.internal.InvalidCallbackException;
import app.telegram.bot.exceptions.internal.InvalidUserInputGroupException;
import app.telegram.bot.exceptions.internal.ListeningMethodCouldNotBeChosenException;
import app.telegram.bot.exceptions.user.InvalidDirectCommandException;
import app.telegram.users.model.InputGroup;
import app.utils.services.contact.exceptions.ContactAlreadyExistsException;
import app.utils.services.contact.exceptions.ContactDoesntExistException;
import app.utils.services.notification.exceptions.NotificationAlreadyExistsException;
import app.utils.services.notification.exceptions.NotificationDoesntExistException;
import app.utils.services.security.exceptions.SecurityUserDoesntExistException;
import app.utils.services.sender.exceptions.SenderApiException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class UpdateReceiverAndExecutor {

    private final MyTelegramLongPollingBot bot;

    private final BotService botService;

    public UpdateReceiverAndExecutor(MyTelegramLongPollingBot bot, Update update, BotService botService) {
        this.bot = bot;
        this.botService = botService;
        onUpdateReceived(update);
    }

    private void onUpdateReceived(Update update) {
        try {
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

            InputGroup inputGroup = botService.getUserNextInputGroupOrBase(chatId);
            log.info("User's input group is-{} for update-{}", inputGroup, update.getUpdateId());

            if (update.hasMessage() && inputGroup == InputGroup.BASE) {
                listenDirectCommandAndExecute(update);
            } else if (update.hasMessage() || (query != null && (query.getData().equals(Callbacks.EMPTY) || Callbacks.isMethod(query.getData()) || Callbacks.isTaskType(query.getData())))) {
                listenUserInputAndExecute(inputGroup, update);
            } else if (query != null) {
                listenCallbackQueryAndExecute(update);
            } else {
                log.error("No listening method chosen for update-{}", update.getUpdateId());
                throw new ListeningMethodCouldNotBeChosenException();
            }
        } catch (SenderApiException e) {
            log.debug("Sender api exception thrown for update-{}", update);
            new SenderUserError(bot, update, botService).execute();
        } catch (SecurityUserDoesntExistException e) {
            log.debug("Security User doesnt exist thrown for update-{}", update);
            new SecurityUserDoesntExistUserError(bot, update, botService).execute();
        } catch (ContactAlreadyExistsException e) {
            log.debug("Contact already exists thrown for update-{}", update);
            new ContactAlreadyExistsUserError(bot, update, botService).execute();
        } catch (ContactDoesntExistException e) {
            log.debug("Contact doesn't exist thrown for update-{}", update);
            new ContactDoesntExistUserError(bot, update, botService).execute();
        } catch (NotificationAlreadyExistsException e) {
            log.debug("Notification already exists thrown for update-{}", update);
            new NotificationAlreadyExistsUserError(bot, update, botService).execute();
        } catch (NotificationDoesntExistException e) {
            log.debug("Notification doesn't exist thrown for update-{}", update);
            new NotificationDoesntExistUserError(bot, update, botService).execute();
        } catch (InvalidDirectCommandException e) {
            log.debug("Invalid direct command thrown for update-{}", update);
            new InvalidDirectCommandUserError(bot, update, botService).execute();
        } catch (InternalErrorException e) {
            log.debug("internal error thrown for update-{}", update);
            e.printStackTrace();
            new InternalError(bot, update, botService).execute();
        } catch (Exception e) {
            log.debug("unknown error thrown for update-{}", update);
            e.printStackTrace();
            new UnknownError(bot, update, botService).execute();
        }
    }

    private void listenDirectCommandAndExecute(Update update) {
        log.info("listenCommandAndExecute was called for update-{}", update.getUpdateId());

        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();
        String customPhrase = "/sendall";

        if (botService.doesUserExist(chatId)) {
            customPhrase = botService.getUserCustomPhrase(update.getMessage().getChatId());
        }

        switch (text) {
            case "/start" -> new StartDirect(bot, update, botService).execute();
            case "/send" -> new SendDirect(bot, update, botService).execute();
            case "/sendall" -> new SendAllDirect(bot, update, botService).execute();
            case "/help" -> new HelpDirect(bot, update, botService).execute();
            case "/task" -> new TaskDirect(bot, update, botService).execute();
            case "/contact" -> new ContactDirect(bot, update, botService).execute();
            case "/notification" -> new NotificationDirect(bot, update, botService).execute();
            case "/clear" -> new ClearDirect(bot, update, botService).execute();
            case "/link" -> new LinkOrUnlinkDirect(bot, update, botService).execute();
            case "/data" -> new DataDirect(bot, update, botService).execute();
            case "/settings" -> new SettingsDirect(bot, update, botService).execute();
            default -> {
                if (text.equals(customPhrase) || text.equals("sendall") || text.equals("send all")) {
                    new SendAllDirect(bot, update, botService).execute();
                } else {
                    throw new InvalidDirectCommandException();
                }
            }
        }
    }

    private void listenCallbackQueryAndExecute(Update update) {
        log.info("listenQueryAndExecute was called for update-{}", update.getUpdateId());
        switch (update.getCallbackQuery().getData()) {
            case Callbacks.REGISTER_YES -> new RegisterYesCallback(bot, update, botService).execute();
            case Callbacks.REGISTER_NO -> new RegisterNoCallback(bot, update, botService).execute();

            case Callbacks.SEND_ONE -> new SendOneCallback(bot, update, botService).execute();
            case Callbacks.SEND_ONE_FINISH -> new SendOneFinishCallback(bot, update, botService).execute();

            case Callbacks.SEND_MANY -> new SendManyCallback(bot, update, botService).execute();
            case Callbacks.SEND_MANY_FINISH -> new SendManyFinishCallback(bot, update, botService).execute();

            case Callbacks.SEND_ALL -> new SendAllCallback(bot, update, botService).execute();

            case Callbacks.TASK_SHOW -> new TaskShowCallback(bot, update, botService).execute();

            case Callbacks.TASK_CREATE -> new TaskCreateCallback(bot, update, botService).execute();
            case Callbacks.TASK_CREATE_FINISH -> new TaskCreateFinishCallback(bot, update, botService).execute();

            case Callbacks.TASK_DELETE -> new TaskDeleteCallback(bot, update, botService).execute();
            case Callbacks.TASK_DELETE_FINISH -> new TaskDeleteFinishCallback(bot, update, botService).execute();

            case Callbacks.TASK_DELETE_ALL -> new TaskDeleteAllCallback(bot, update, botService).execute();
            case Callbacks.TASK_DELETE_ALL_FINISH -> new TaskDeleteAllFinishCallback(bot, update, botService).execute();

            case Callbacks.CONTACT_ADD -> new ContactAddCallback(bot, update, botService).execute();
            case Callbacks.CONTACT_ADD_FINISH -> new ContactAddFinishCallback(bot, update, botService).execute();

            case Callbacks.CONTACT_REMOVE_ONE -> new ContactRemoveOneCallback(bot, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_ONE_FINISH ->
                    new ContactRemoveOneFinishCallback(bot, update, botService).execute();

            case Callbacks.CONTACT_REMOVE_MANY -> new ContactRemoveManyCallback(bot, update, botService).execute();
            case Callbacks.CONTACT_REMOVE_MANY_FINISH ->
                    new ContactRemoveManyFinishCallback(bot, update, botService).execute();

            case Callbacks.NOTIFICATION_ADD -> new NotificationAddCallback(bot, update, botService).execute();
            case Callbacks.NOTIFICATION_ADD_FINISH ->
                    new NotificationAddFinishCallback(bot, update, botService).execute();

            case Callbacks.NOTIFICATION_REMOVE_ONE ->
                    new NotificationRemoveOneCallback(bot, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_ONE_FINISH -> new NotificationRemoveOneFinishCallback(bot, update,
                    botService).execute();

            case Callbacks.NOTIFICATION_REMOVE_MANY ->
                    new NotificationRemoveManyCallback(bot, update, botService).execute();
            case Callbacks.NOTIFICATION_REMOVE_MANY_FINISH -> new NotificationRemoveManyFinishCallback(bot, update,
                    botService).execute();

            case Callbacks.DATA_SHOW -> new DataShowCallback(bot, update, botService).execute();

            case Callbacks.DATA_REMOVE -> new DataRemoveCallback(bot, update, botService).execute();
            case Callbacks.DATA_REMOVE_FINISH -> new DataRemoveCallbackFinish(bot, update, botService).execute();

            case Callbacks.CLEAR -> new ClearCallback(bot, update, botService).execute();

            case Callbacks.LINK -> new LinkCallback(bot, update, botService).execute();
            case Callbacks.LINK_FINISH -> new LinkFinishCallback(bot, update, botService).execute();

            case Callbacks.UNLINK -> new UnlinkCallback(bot, update, botService).execute();

            case Callbacks.SETTINGS_ACTION_CONFIRMATION ->
                    new ActionConfirmationCallback(bot, update, botService).execute();
            case Callbacks.SETTINGS_ACTION_CONFIRMATION_ENABLE -> new ActionConfirmationEnableCallback(bot, update,
                    botService).execute();
            case Callbacks.SETTINGS_ACTION_CONFIRMATION_DISABLE -> new ActionConfirmationDisableCallback(bot, update,
                    botService).execute();

            case Callbacks.SETTINGS_CUSTOM_PHRASE -> new CustomPhraseCallback(bot, update, botService).execute();
            case Callbacks.SETTINGS_CUSTOM_PHRASE_FINISH ->
                    new CustomPhraseFinishCallback(bot, update, botService).execute();

            case Callbacks.CANCEL -> new CancelCallback(bot, update, botService).execute();

            default -> throw new InvalidCallbackException();
        }
    }

    private void listenUserInputAndExecute(InputGroup inputGroup, Update update) {
        log.info("listenUserInputAndExecute was called for update-{} and inputGroup-{}", update.getUpdateId(), inputGroup);
        switch (inputGroup) {
            case SEND_ONE -> new SendOneChain(bot, update, botService).execute();
            case SEND_MANY -> new SendManyChain(bot, update, botService).execute();

            case TASK_CREATE -> new TaskCreateChain(bot, update, botService).execute();
            case TASK_DELETE -> new TaskDeleteChain(bot, update, botService).execute();

            case CONTACT_ADD_ONE -> new ContactAddChain(bot, update, botService).execute();
            case CONTACT_REMOVE_ONE -> new ContactRemoveOneChain(bot, update, botService).execute();
            case CONTACT_REMOVE_MANY -> new ContactRemoveManyChain(bot, update, botService).execute();

            case NOTIFICATION_ADD_ONE -> new NotificationAddChain(bot, update, botService).execute();
            case NOTIFICATION_REMOVE_ONE -> new NotificationRemoveOneChain(bot, update, botService).execute();
            case NOTIFICATION_REMOVE_MANY -> new NotificationRemoveManyChain(bot, update, botService).execute();

            case CUSTOM_PHRASE -> new CustomPhraseChain(bot, update, botService).execute();

            case LINK -> new LinkChain(bot, update, botService).execute();

            default -> throw new InvalidUserInputGroupException();
        }
    }
}
