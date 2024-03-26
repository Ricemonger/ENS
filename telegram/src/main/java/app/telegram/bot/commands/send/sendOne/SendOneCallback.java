package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.send.SendStage1AskMethod;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneCallback extends AbstractBotCommand {

    public SendOneCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.SEND_ONE);

        new SendStage1AskMethod(bot, update, botService).execute();
    }
}
