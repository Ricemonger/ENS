package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.linking.Stage2WriteUsernameAndAskPassword;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkChain extends AbstractBotCommand {

    public LinkChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        InputState inputState = botService.geUserInputState(chatId);
        switch (inputState) {
            case LINK_USERNAME -> new Stage2WriteUsernameAndAskPassword(bot, update, botService).execute();
            case LINK_PASSWORD -> new LinkFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
