package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomPhraseFinishCallback extends AbstractBotCommand {

    public CustomPhraseFinishCallback(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        botService.setCustomPhraseFromInputMap(chatId);
        sendAnswer("Your custom phrase was successfully set!");
    }
}
