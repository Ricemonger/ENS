package app.telegram.bot.commands.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.Callbacks;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkDirect extends AbstractBotCommand {

    public LinkDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        if (!isAccountLinked()) {
            String answer = "Would tou like to link your telegram account to existing ENS account?";
            askYesOrNoFromInlineKeyboard(answer, Callbacks.LINK, Callbacks.CANCEL);
        } else {
            String answer = "You already have linked ENS Account. Would you like to unlink it?";
            askYesOrNoFromInlineKeyboard(answer, Callbacks.UNLINK, Callbacks.CANCEL);
        }
    }

    private boolean isAccountLinked() {
        return botService.isLinked(chatId);
    }
}
