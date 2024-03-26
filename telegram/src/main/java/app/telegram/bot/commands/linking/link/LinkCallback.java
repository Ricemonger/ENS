package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkCallback extends AbstractBotCommand {

    public LinkCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.LINK);

        new LinkStage1AskUsername(bot, update, botService).execute();
    }
}
