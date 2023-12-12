package app.telegram.bot.commands.send.sendMany;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.bot.commands.send.SendStage2WriteMethodAskContactId;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class SendManyChain extends AbstractBotCommand {

    public SendManyChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        InputState inputState = botService.getUserNextInputStateOrBase(chatId);
        switch (inputState) {
            case CONTACT_METHOD -> new SendStage2WriteMethodAskContactId(bot, update, botService).execute();
            case CONTACT_ID -> new SendManyStage3WriteContactIdAskNotificationName(bot, update, botService).execute();
            case NOTIFICATION_NAME -> new SendManyFinish(bot, update, botService).execute();
            default -> new CancelCallback(bot, update, botService).execute();
        }
    }
}
