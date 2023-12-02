package app.telegram.bot.commands.send;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendStage1AskMethod extends AbstractBotCommand {

    public SendStage1AskMethod(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processFirstInput(chatId, InputState.CONTACT_METHOD, "Please input one of contact's method:");
    }
}
