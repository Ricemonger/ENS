package app.telegram.bot.commands.linking;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage1AskUsername extends AbstractBotCommand {

    public Stage1AskUsername(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setNextInput(chatId, InputState.USERNAME);
        sendAnswer("Please input your ENS account's Username:");
    }
}
