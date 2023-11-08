package app.telegram.mytgbot.commands.directCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkBotCommand extends AbstractBotCommand {

    public LinkBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        if (!isAccountLinked()) {
            sendAnswer("Would tou like to link your telegram account to existing ENS account?");
            askYesOrNoFromInlineKeyboard("LINK_ADD_YES", "LINK_ADD_NO");
        } else {
            sendAnswer("You already have linked ENS account");
        }
    }

    private boolean isAccountLinked() {
        return botService.isLinked(chatId);
    }
}
