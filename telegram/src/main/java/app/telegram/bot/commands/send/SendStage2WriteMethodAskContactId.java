package app.telegram.bot.commands.send;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendStage2WriteMethodAskContactId extends AbstractBotCommand {

    public SendStage2WriteMethodAskContactId(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processInput(InputState.CONTACT_METHOD, InputState.CONTACT_ID, "Please input contact's identifier" +
                "(Phone number/Email/etc.):");
    }
}
