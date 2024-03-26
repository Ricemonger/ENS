package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactStage2WriteMethodAndAskId extends AbstractBotCommand {

    public ContactStage2WriteMethodAndAskId(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processMiddleInput(InputState.CONTACT_METHOD, InputState.CONTACT_ID, "Please input ContactId(Phone or Email):");
    }
}
