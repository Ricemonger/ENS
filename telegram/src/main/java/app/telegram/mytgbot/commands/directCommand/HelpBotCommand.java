package app.telegram.mytgbot.commands.directCommand;

import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.mytgbot.commands.TelegramBotCommandsConfiguration;
import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpBotCommand extends AbstractBotCommand {

    public HelpBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = TelegramBotCommandsConfiguration.HELP_MESSAGE;
        sendAnswer(answer);
    }
}
