package app.telegram.bot.commands.cancel;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CancelCallback extends AbstractBotCommand {

    public CancelCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        botService.setNextInput(chatId, InputState.BASE);
        botService.setNextInputGroup(chatId, InputGroup.BASE);
        botService.clearInputs(chatId);
        sendAnswer("Operation Cancelled");
    }
}
