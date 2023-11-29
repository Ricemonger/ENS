package app.telegram.bot.commands.linking;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Stage3WritePasswordFinishAndPrint extends AbstractBotCommand {

    public Stage3WritePasswordFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processInput(InputState.PASSWORD, InputState.BASE);

        String[] s = botService.getUsernameAndPasswordFromInputMap(chatId);

        sendAnswer("Input is finished. Your ENS account is:" + s[0] + "|" + s[1]);

        botService.setNextInputGroup(chatId, InputGroup.BASE);
    }
}
