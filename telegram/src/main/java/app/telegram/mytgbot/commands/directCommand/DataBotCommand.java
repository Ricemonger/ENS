package app.telegram.mytgbot.commands.directCommand;

import app.telegram.model.BotService;
import app.telegram.mytgbot.TelegramBotCommandsConfiguration;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DataBotCommand extends AbstractBotCommand {

    public DataBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = TelegramBotCommandsConfiguration.DATA_HELP_MESSAGE;
        sendAnswer(answer);
        //TODO KEYBOARD DATA
    }
}
