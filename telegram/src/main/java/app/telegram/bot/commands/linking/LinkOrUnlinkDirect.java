package app.telegram.bot.commands.linking;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkOrUnlinkDirect extends AbstractBotCommand {

    public LinkOrUnlinkDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        MyFunctionalInterface function = () -> {
            if (!isAccountLinked()) {
                String answer = "Would you like to link your telegram account to existing ENS account?";
                askYesOrNoFromInlineKeyboard(answer, Callbacks.LINK, Callbacks.CANCEL);
            } else {
                String answer = "You already have linked ENS Account. Would you like to unlink it?";
                askYesOrNoFromInlineKeyboard(answer, Callbacks.UNLINK, Callbacks.CANCEL);
            }
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }

    private boolean isAccountLinked() {
        return botService.isLinked(chatId);
    }
}
