package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkStage1AskUsername extends AbstractBotCommand {

    public LinkStage1AskUsername(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processFirstInput(chatId, InputState.USERNAME, "Please input your ENS account's Username:");
    }
}
