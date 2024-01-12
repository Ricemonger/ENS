package app.telegram.bot.commands.linking.link;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LinkStage3WritePasswordFinishAndPrint extends AbstractBotCommand {

    public LinkStage3WritePasswordFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        processLastInput(InputState.PASSWORD);

        String[] s = botService.getUsernameAndPasswordFromInputsMap(chatId);

        sendText("Input is finished. Your ENS account is:" + s[0] + "|" + s[1]);
    }
}
