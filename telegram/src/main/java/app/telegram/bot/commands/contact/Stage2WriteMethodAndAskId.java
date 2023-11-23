package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage2WriteMethodAndAskId extends AbstractBotCommand {

    public Stage2WriteMethodAndAskId(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        processInput(InputState.CONTACT_METHOD, InputState.CONTACT_CONTACT_ID, "Please input ContactId(Phone or Email):");
    }
}
