package app.telegram.bot.commands.linking;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage2WriteUsernameAndAskPassword extends AbstractBotCommand {

    public Stage2WriteUsernameAndAskPassword(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processInput(InputState.LINK_USERNAME, InputState.LINK_PASSWORD, "Please input your ENS account's Password:");
    }
}
