package app.telegram.bot.commands.send;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import app.utils.feign_clients.contact.Method;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class SendStage1AskMethod extends AbstractBotCommand {

    public SendStage1AskMethod(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.setNextInputState(chatId, InputState.CONTACT_METHOD);
        sendAnswer("Please input one of contact's method" + Arrays.asList(Method.values()) + ":");
    }
}
