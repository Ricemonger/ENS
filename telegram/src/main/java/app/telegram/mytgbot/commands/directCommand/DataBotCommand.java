package app.telegram.mytgbot.commands.directCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.mytgbot.commands.TelegramBotCommandsConfiguration;
import app.telegram.service.BotService;
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
