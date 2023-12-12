package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.cancel.CancelCallback;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class CustomPhraseChain extends AbstractBotCommand {

    public CustomPhraseChain(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        InputState inputState = botService.getNextInputStateOrBase(chatId);
        if (Objects.requireNonNull(inputState) == InputState.CUSTOM_PHRASE) {
            new CustomPhraseFinish(bot, update, botService).execute();
        } else {
            new CancelCallback(bot, update, botService).execute();
        }
    }
}
