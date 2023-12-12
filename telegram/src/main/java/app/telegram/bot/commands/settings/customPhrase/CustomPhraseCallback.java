package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.users.model.InputGroup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomPhraseCallback extends AbstractBotCommand {

    public CustomPhraseCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.setNextInputGroup(chatId, InputGroup.CUSTOM_PHRASE);
        new CustomPhraseStage1AskPhrase(bot, update, botService).execute();
    }
}
