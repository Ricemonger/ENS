package app.telegram.mytgbot.commands;

import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
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
        InlineKeyboardMarkup inlineKeyboardMarkup = createYesOrNoInlineKeyboardMarkup(yesCallbackData, noCallbackData);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        executeMessage(sendMessage);
    }

    protected final InlineKeyboardMarkup createYesOrNoInlineKeyboardMarkup(String yesCallbackData, String noCallbackData) {
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
        return botService.doesUserExists(chatId);
    }

    protected final void sendOperationCancelledMessage() {
        String answer = "Operation was cancelled";
        sendAnswer(answer);
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
