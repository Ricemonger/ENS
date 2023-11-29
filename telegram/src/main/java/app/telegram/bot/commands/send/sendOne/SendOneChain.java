package app.telegram.bot.commands.send.sendOne;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.send.SendStage2WriteMethodAskContactId;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendOneChain extends AbstractBotCommand {

    public SendOneChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        InputState inputState = botService.geUserInputState(chatId);
        switch (inputState) {
            case CONTACT_METHOD -> new SendStage2WriteMethodAskContactId(bot, update, botService).execute();
            case CONTACT_ID -> new SendOneStage3WriteContactIdAskNotificationText(bot, update, botService).execute();
            case NOTIFICATION_TEXT -> new SendOneFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
