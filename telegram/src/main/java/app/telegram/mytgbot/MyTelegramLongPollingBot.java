package app.telegram.mytgbot;

import app.telegram.config.TelegramBotAuthorizationConfiguration;
import app.telegram.config.TelegramBotConfiguration;
import app.telegram.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Component
public class MyTelegramLongPollingBot extends TelegramLongPollingBot {

    private final TelegramBotConfiguration config;

    private final TelegramBotAuthorizationConfiguration authConfig;

    private final BotService botService;

    @Autowired
    public MyTelegramLongPollingBot(TelegramBotAuthorizationConfiguration authConfig
            , TelegramBotConfiguration config
            , BotService botService) {
        super(authConfig.getAPI_TOKEN());
        this.config = config;
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
            Message message = update.getMessage();
            String messageText = message.getText();
            switch (messageText) {
                case "/start" -> onStartCommand(message);
                case "/send" -> onSendCommand(message);
                case "/sendall" -> onSendAllCommand(message);
                case "/help" -> onHelpCommand(message);
                case "/contact" -> onContactCommand(message);
                case "/notification" -> onNotificationCommand(message);
                case "/clear" -> onClearCommand(message);
                case "/link" -> onLinkCommand(message);
                case "/data" -> onDataCommand(message);
                default -> onInvalidCommand(message);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String query = callbackQuery.toString();
            Long chatId = callbackQuery.getMessage().getChatId();
            switch (query) {
                case "REGISTER_YES" -> onRegisterYesQuery(chatId);
                case "REGISTER_NO" -> onRegisterNoQuery(chatId);
                case "SEND_ALL_YES" -> onSendAllYesQuery(chatId);
                case "SEND_ALL_NO" -> onSendAllNoQuery(chatId);
                case "CLEAR_YES" -> onClearYesQuery(chatId);
                case "CLEAR_NO" -> onClearNoQuery(chatId);
                default -> onInvalidQuery(chatId);
            }
        }
    }

    private void onStartCommand(Message message) {
        Long chatId = message.getChatId();
        //TODO welcoming message
        String answer = "Welcome to Emergency Notification Service Bot!";
        sendAnswer(chatId, answer);
        checkUserInDbAndAskToRegisterIfNot(chatId);
    }

    private void onSendCommand(Message message) {
        Long chatId = message.getChatId();
        MyFunctionalInterface function = () -> {
            String answer = "Choose your action:";
            //TODO
            sendAnswer(chatId, answer);
        };
        executeCommandIfUserExistsOrAskToRegister(chatId, function);
    }

    private void onSendAllCommand(Message message) {
        Long chatId = message.getChatId();
        MyFunctionalInterface function = () -> {
            String answer = "Do you really wand to send notification to all your emergency contacts?";
            sendAnswer(chatId, answer);
            askYesOrNoFromInlineKeyboard(chatId, "SEND_ALL_YES", "SEND_ALL_NO");
        };
        executeCommandIfUserExistsOrAskToRegister(chatId, function);
    }

    private void onSendAllYesQuery(Long chatId) {
        botService.sendAll(chatId);
        String answer = "All notifications were successfully send";
    }

    private void onSendAllNoQuery(Long chatId) {
        sendOperationCancelledMessage(chatId);
    }

    private void onHelpCommand(Message message) {
        Long chatId = message.getChatId();
        String answer = TelegramBotConfiguration.HELP_MESSAGE;
        sendAnswer(chatId, answer);
    }

    private void onContactCommand(Message message) {
        Long chatId = message.getChatId();
        String answer = TelegramBotConfiguration.CONTACT_HELP_MESSAGE;
        sendAnswer(chatId, answer);
        //TODO KEYBOARD CONTACT
    }

    private void onNotificationCommand(Message message) {
        Long chatId = message.getChatId();
        String answer = TelegramBotConfiguration.NOTIFICATION_HELP_MESSAGE;
        sendAnswer(chatId, answer);
        //TODO KEYBOARD NOTIFICATION
    }

    private void onClearCommand(Message message) {
        Long chatId = message.getChatId();
        MyFunctionalInterface function = () -> {
            String answer = "Do you really want to clear all your Contacts and Notifications?";
            sendAnswer(chatId, answer);
            askYesOrNoFromInlineKeyboard(chatId, "CLEAR_YES", "CLEAR_NO");
        };
        executeCommandIfUserExistsOrAskToRegister(chatId, function);
    }

    private void onClearYesQuery(Long chatId) {
        String answer = "All  Contacts and Notifications were cleared";
        botService.clear(chatId);
        sendAnswer(chatId, answer);
    }

    private void onClearNoQuery(Long chatId) {
        sendOperationCancelledMessage(chatId);
    }

    private void sendOperationCancelledMessage(Long chatId) {
        String answer = "Operation was cancelled";
        sendAnswer(chatId, answer);
    }

    private void onLinkCommand(Message message) {
        Long chatId = message.getChatId();
        MyFunctionalInterface function = () -> {
            //TODO LINKING
        };
        executeCommandIfUserExistsOrAskToRegister(chatId, function);
    }

    private void onDataCommand(Message message) {
        Long chatId = message.getChatId();
        String answer = TelegramBotConfiguration.DATA_HELP_MESSAGE;
        sendAnswer(chatId, answer);
        //TODO KEYBOARD DATA
    }

    private void onInvalidCommand(Message message) {
        String answer = "Sorry, command is not recognized";
        sendAnswer(message.getChatId(), answer);
    }

    private void onInvalidQuery(Long chatId) {
        String answer = "Sorry, query is not recognized";
        sendAnswer(chatId, answer);
    }

    private void executeCommandIfUserExistsOrAskToRegister(Long chatId, MyFunctionalInterface functionalInterface) {
        if (isUserInDb(chatId)) {
            functionalInterface.executeCommand();
        } else {
            checkUserInDbAndAskToRegisterIfNot(chatId);
        }
    }

    @FunctionalInterface
    interface MyFunctionalInterface {
        void executeCommand();
    }

    private void checkUserInDbAndAskToRegisterIfNot(Long chatId) {
        if (!isUserInDb(chatId)) {
            String answer = "You're not registered in bot.\n Would you like to register?";
            sendAnswer(chatId, answer);
            askYesOrNoFromInlineKeyboard(chatId, "REGISTER_YES", "REGISTER_NO");
        }
    }

    private void onRegisterYesQuery(Long chatId) {
        String answer = "Registering you in Bot's Database...";
        sendAnswer(chatId, answer);
        addUserToDb(chatId);
    }

    private void onRegisterNoQuery(Long chatId) {
        String answer = "You chose not to register in bot's database.\n Most of the functionality will not be " +
                "available for you, but you can register at any time";
        sendAnswer(chatId, answer);
    }

    private void askYesOrNoFromInlineKeyboard(Long chatId, String yesCallbackData, String noCallbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createYesOrNoInlineKeyboardMarkup(yesCallbackData, noCallbackData);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        executeMessage(sendMessage);
    }

    private InlineKeyboardMarkup createYesOrNoInlineKeyboardMarkup(String yesCallbackData, String noCallbackData) {
        InlineKeyboardButton inlineYesButton = new InlineKeyboardButton("Yes");
        inlineYesButton.setCallbackData(yesCallbackData);
        InlineKeyboardButton inlineNoButton = new InlineKeyboardButton("No");
        inlineNoButton.setCallbackData(noCallbackData);

        List<InlineKeyboardButton> inlineButtonsList = new ArrayList<>();
        inlineButtonsList.add(inlineYesButton);
        inlineButtonsList.add(inlineNoButton);

        List<List<InlineKeyboardButton>> inlineButtonRowsList = Collections.singletonList(inlineButtonsList);
        return new InlineKeyboardMarkup(inlineButtonRowsList);
    }

    private void addUserToDb(Long chatId) {
        String answer;
        if (!isUserInDb(chatId)) {
            botService.create(chatId);
            answer = "You were successfully registered!";
        } else {
            answer = "You are already registered";
        }
        sendAnswer(chatId, answer);
    }

    private boolean isUserInDb(Long chatId) {
        return botService.doesUserExists(chatId);
    }

    private void sendAnswer(Long chatId, String answer) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
        executeMessage(sendMessage);
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

