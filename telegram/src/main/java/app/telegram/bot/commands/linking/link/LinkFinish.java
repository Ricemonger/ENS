package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import app.telegram.bot.commands.linking.Stage3WritePasswordFinishAndPrint;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkFinish extends AbstractBotCommand {

    public LinkFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        new Stage3WritePasswordFinishAndPrint(bot, update, botService).execute();

        askYesOrNoFromInlineKeyboard("Would you link to confirm linking?", Callbacks.LINK_FINISH, Callbacks.CANCEL);
    }
}
