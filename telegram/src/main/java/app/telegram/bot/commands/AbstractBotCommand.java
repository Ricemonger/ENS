package app.telegram.bot.commands;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import app.utils.services.contact.Method;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractBotCommand {

    protected final TelegramLongPollingBot bot;

    protected final Update update;

    protected final BotService botService;

    protected final Long chatId;

    public AbstractBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        this.bot = bot;
        this.update = update;
        this.botService = botService;
        if (update.hasMessage()) {
            this.chatId = update.getMessage().getChatId();
        } else {
            this.chatId = update.getCallbackQuery().getMessage().getChatId();
        }
    }

    public final void execute() {
        log.debug("execute called for command - {}", this);
        executeCommand();
        log.trace("execute executed for command - {}", this);
    }

    protected abstract void executeCommand();

    protected final void setUserActionConfirmFlag(boolean flag) {
        botService.setActionConfirmFlag(chatId, flag);
    }

    protected final boolean isUserActionConfirmFlag() {
        return botService.getUserActionConfirmFlag(chatId);
    }

    protected final void setCustomPhrase(Long chatId, String customPhrase) {
        botService.setUserCustomPhrase(chatId, customPhrase);
    }

    protected final String getCustomPhrase(Long chatId) {
        return botService.getUserCustomPhrase(chatId);
    }

    protected final void processFirstInput(Long chatId, InputState nextState, String question) {
        botService.setNextInputState(chatId, nextState);

        if (nextState == InputState.CONTACT_METHOD) {
            askForMethodFromInlineKeyboard(question);
        } else {
            askForInputOrEmptyLineFromInlineKeyboard(question);
        }
    }

    protected final void processMiddleInput(InputState currentState, InputState nextState, String question) {
        saveCurrentInputAndSetNextState(currentState, nextState);

        if (nextState == InputState.CONTACT_METHOD) {
            askForMethodFromInlineKeyboard(question);
        } else {
            askForInputOrEmptyLineFromInlineKeyboard(question);
        }
    }

    protected final void processLastInput(InputState currentState) {
        saveCurrentInputAndSetNextState(currentState, InputState.BASE);
        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }

    private void saveCurrentInputAndSetNextState(InputState currentState, InputState nextState) {
        String userInput;

        if (update.hasMessage()) {
            userInput = update.getMessage().getText();
        } else if (currentState == InputState.CONTACT_METHOD) {
            String data = update.getCallbackQuery().getData();
            switch (data) {
                case Callbacks.METHOD_SMS -> userInput = Method.SMS.name();
                case Callbacks.METHOD_VIBER -> userInput = Method.VIBER.name();
                case Callbacks.METHOD_EMAIL -> userInput = Method.EMAIL.name();
                case Callbacks.METHOD_TELEGRAM -> userInput = Method.TELEGRAM.name();
                default -> userInput = "";
            }
        } else {
            userInput = "";
        }
        botService.saveInput(chatId, currentState, userInput);
        botService.setNextInputState(chatId, nextState);
    }

    private void askForInputOrEmptyLineFromInlineKeyboard(String text) {
        CallbackButton button = new CallbackButton("INPUT EMPTY LINE", Callbacks.EMPTY);
        askFromInlineKeyboard(text, 1, button);
    }

    private void askForMethodFromInlineKeyboard(String question) {
        CallbackButton sms = new CallbackButton("SMS", Callbacks.METHOD_SMS);
        CallbackButton viber = new CallbackButton("VIBER", Callbacks.METHOD_VIBER);
        CallbackButton email = new CallbackButton("EMAIL", Callbacks.METHOD_EMAIL);
        CallbackButton telegram = new CallbackButton("TELEGRAM", Callbacks.METHOD_TELEGRAM);
        askFromInlineKeyboard(question, 1, sms, viber, email, telegram);
    }

    protected final void executeCommandIfUserExistsOrAskToRegister(MyFunctionalInterface functionalInterface) {
        if (isUserInDb(chatId)) {
            functionalInterface.executeCommand();
        } else {
            askUserToRegister();
        }
    }

    protected final void askUserToRegister() {
        String answer = "You're not registered in bot.\n Would you like to register?";
        askYesOrNoFromInlineKeyboard(answer, Callbacks.REGISTER_YES, Callbacks.REGISTER_NO);
    }

    protected final void askYesOrNoFromInlineKeyboard(String text, String yesCallbackData,
                                                      String noCallbackData) {
        CallbackButton yesButton = new CallbackButton("Yes", yesCallbackData);
        CallbackButton noButton = new CallbackButton("No", noCallbackData);
        askFromInlineKeyboard(text, 2, yesButton, noButton);
    }

    protected final void askFromInlineKeyboard(String text, int buttonsInLine, CallbackButton... buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(buttonsInLine, buttons);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        executeMessage(sendMessage);
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup(int buttonsInLine,
                                                            CallbackButton... callbackButtons) {
        List<InlineKeyboardButton> inlineButtonsList = new ArrayList<>();
        List<List<InlineKeyboardButton>> inlineButtonRowsList = new ArrayList<>();

        int counter = 0;
        for (CallbackButton button : callbackButtons) {
            counter++;
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(button.text());
            inlineButton.setCallbackData(button.data());
            inlineButtonsList.add(inlineButton);
            if (counter % buttonsInLine == 0) {
                inlineButtonRowsList.add(List.copyOf(inlineButtonsList));
                inlineButtonsList.clear();
            }
        }
        if (!inlineButtonsList.isEmpty()) {
            inlineButtonRowsList.add(inlineButtonsList);
        }

        return new InlineKeyboardMarkup(inlineButtonRowsList);
    }

    protected final void addUserToDb() {
        String answer;
        if (!isUserInDb(chatId)) {
            botService.create(chatId);
            answer = "You were successfully registered!";
        } else {
            answer = "You are already registered";
        }
        sendAnswer(answer);
    }

    protected final boolean isUserInDb(Long chatId) {
        return botService.doesUserExist(chatId);
    }

    protected final void sendAnswer(String answer) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
        executeMessage(sendMessage);
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%s(bot=%s,update=%s,botService=%s,chatId=%s)", this.getClass().getSimpleName(), bot, update, botService,
                chatId);
    }

    @FunctionalInterface
    protected interface MyFunctionalInterface {
        void executeCommand();
    }
}
