package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomPhraseStage2WritePhraseFinishAndPrint extends AbstractBotCommand {

    public CustomPhraseStage2WritePhraseFinishAndPrint(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processLastInput(InputState.CUSTOM_PHRASE);
    }
}
