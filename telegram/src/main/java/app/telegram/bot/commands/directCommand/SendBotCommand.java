package app.telegram.bot.commands.directCommand;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendBotCommand extends AbstractBotCommand {

    public SendBotCommand(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        MyFunctionalInterface function = () -> {
            String answer = "Choose your action:";
            //TODO
            sendAnswer(answer);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
