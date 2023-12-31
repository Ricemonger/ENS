package app.telegram.bot.commands.contact.removeMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.contact.ContactStage2WriteMethodAndAskId;
import app.telegram.bot.commands.contact.ContactStage3WriteIdAndAskNotification;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactRemoveManyChain extends AbstractBotCommand {

    public ContactRemoveManyChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void executeCommand() {
        InputState inputState = botService.getUserNextInputStateOrBase(chatId);
        switch (inputState) {
            case CONTACT_METHOD -> new ContactStage2WriteMethodAndAskId(bot, update, botService).execute();
            case CONTACT_ID -> new ContactStage3WriteIdAndAskNotification(bot, update, botService).execute();
            case NOTIFICATION_NAME -> new ContactRemoveManyFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
