package app.telegram.bot.commands.contact.removeOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveOneStage3WriteContactIdFinishAndPrint extends AbstractBotCommand {

    public ContactRemoveOneStage3WriteContactIdFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.CONTACT_ID);

        sendAnswer("Your contact is:" + botService.getContactFromInputsMap(chatId));
    }
}
