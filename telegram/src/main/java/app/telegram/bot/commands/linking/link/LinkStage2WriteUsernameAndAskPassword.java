package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkStage2WriteUsernameAndAskPassword extends AbstractBotCommand {

    public LinkStage2WriteUsernameAndAskPassword(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processMiddleInput(InputState.USERNAME, InputState.PASSWORD, "Please input your ENS account's Password:");
    }
}
