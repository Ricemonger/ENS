package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactStage1AskMethod extends AbstractBotCommand {

    public ContactStage1AskMethod(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        botService.setNextInputState(chatId, InputState.CONTACT_METHOD);
        sendAnswer("Please input Contact Method[SMS,VIBER,EMAIL,TELEGRAM]:");
    }
}
