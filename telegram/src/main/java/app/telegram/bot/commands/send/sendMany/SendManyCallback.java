package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.send.SendStage1AskMethod;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendManyCallback extends AbstractBotCommand {

    public SendManyCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setUserNextInputGroup(chatId, InputGroup.SEND_MANY);

        new SendStage1AskMethod(bot, update, botService).execute();
    }
}
