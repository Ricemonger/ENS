package app.telegram.mytgbot.commands.directCommand;

import app.telegram.mytgbot.TelegramBotCommandsConfiguration;
import app.telegram.mytgbot.commands.AbstractBotCommand;
import app.telegram.service.BotService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactBotCommand extends AbstractBotCommand {

    public ContactBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        String answer = TelegramBotCommandsConfiguration.CONTACT_HELP_MESSAGE;
        sendAnswer(answer);
        //TODO KEYBOARD CONTACT
    }
}
