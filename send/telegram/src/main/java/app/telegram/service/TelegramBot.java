package app.telegram.service;

import app.telegram.config.TelegramBotConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotConfiguration configuration;

    private final List<BotCommand> botCommandList = new ArrayList<>();

    @Autowired
    public TelegramBot(TelegramBotConfiguration configuration) {
        super(configuration.getAPI_TOKEN());
        this.configuration = configuration;
        botCommandList.add(new BotCommand("/start", "Starts process of communication with bot"));
        botCommandList.add(new BotCommand("/mydata", "View all your data stored in our database"));
        botCommandList.add(new BotCommand("/clearmydata", "Remove most of your data, except your account details"));
        botCommandList.add(new BotCommand("/deletemydata", "Fully delete ALL of your data"));
        botCommandList.add(new BotCommand("/help", "Get expanded description of all bot commands"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return configuration.getBOT_NAME();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.hasMessage()) {
            Message message = update.getMessage();
            String messageText = message.getText();
            if (messageText.equals("/start")) {
                onStartCommand(message);
            } else {
                onInvalidCommand(message);
            }
        }
    }

    private void onStartCommand(Message message) {
        String answer = "Emergency Notification System Bot is initialized and ready to use";
        sendMessage(message.getChatId(), answer);
    }

    private void onInvalidCommand(Message message) {
        String answer = "Sorry, command is not recognized";
        sendMessage(message.getChatId(), answer);
    }

    private void sendMessage(Long chatId, String answer) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("start");
        keyboardRow.add("help");
        keyboardRow.add("data");
        keyboardRows.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Exception {} was thrown, params chatId-{}, answer-{}", e, chatId, answer);
            e.printStackTrace();
        }
    }
}
