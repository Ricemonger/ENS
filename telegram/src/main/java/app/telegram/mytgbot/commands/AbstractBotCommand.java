package app.telegram.mytgbot.commands;

import app.telegram.model.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBotCommand {

    protected final TelegramLongPollingBot bot;

    protected final Update update;

    protected final BotService botService;

    protected final Long chatId;

    public AbstractBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        this.bot = bot;
        this.update = update;
        this.botService = botService;
        this.chatId = update.getMessage().getChatId();
    }

    public abstract void execute();

    protected final void executeCommandIfUserExistsOrAskToRegister(MyFunctionalInterface functionalInterface) {
        if (isUserInDb(chatId)) {
            functionalInterface.executeCommand();
        } else {
            checkUserInDbAndAskToRegisterIfNot();
        }
    }

    protected final void checkUserInDbAndAskToRegisterIfNot() {
        if (!isUserInDb(chatId)) {
            String answer = "You're not registered in bot.\n Would you like to register?";
            sendAnswer(answer);
            askYesOrNoFromInlineKeyboard("REGISTER_YES", "REGISTER_NO");
        }
    }

    protected final void askYesOrNoFromInlineKeyboard(String yesCallbackData, String noCallbackData) {
        CallbackButton yesButton = new CallbackButton("Yes", yesCallbackData);
        CallbackButton noButton = new CallbackButton("No", noCallbackData);
        askFromInlineKeyboard(2, yesButton, noButton);
    }

    protected final void askFromInlineKeyboard(int buttonsInLine, CallbackButton... buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardMarkup(buttonsInLine, buttons);

        SendMessage sendMessage = new SendMessage();
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
                inlineButtonRowsList.add(inlineButtonsList);
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

    protected final void executeMessage(SendMessage sendMessage) {
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    protected interface MyFunctionalInterface {
        void executeCommand();
    }
}
