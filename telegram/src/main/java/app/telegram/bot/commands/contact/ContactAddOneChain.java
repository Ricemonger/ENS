package app.telegram.bot.commands.contact;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ContactAddOneChain extends AbstractBotCommand {

    public ContactAddOneChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    public void execute() {
        InputState inputState = botService.geUserInputState(chatId);
        switch (inputState) {
            case CONTACT_METHOD -> new Stage2WriteMethodAndAskId(bot, update, botService).execute();
            case CONTACT_CONTACT_ID -> new Stage3WriteIdAndAskNotification(bot, update, botService).execute();
            case CONTACT_NOTIFICATION_NAME -> new ContactAddOneFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
