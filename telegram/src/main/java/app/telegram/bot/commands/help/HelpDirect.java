package app.telegram.bot.commands.help;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpDirect extends AbstractBotCommand {

    public HelpDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        String answer = BotCommandsConfig.HELP_MESSAGE;
        sendText(answer);
    }
}
