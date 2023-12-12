package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputState;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomPhraseStage1AskPhrase extends AbstractBotCommand {

    public CustomPhraseStage1AskPhrase(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        processFirstInput(chatId, InputState.CUSTOM_PHRASE, "Please enter new custom phrase:");
    }
}
