package app.telegram.bot.commands.settings.customPhrase;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomPhraseFinish extends AbstractBotCommand {

    public CustomPhraseFinish(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        new CustomPhraseStage2WritePhraseFinishAndPrint(bot, update, botService).execute();

        String phrase = botService.getCustomPhraseFromInputMap(chatId);

        String question = String.format("Your new custom phrase is:%s. Would you like to confirm?", phrase);

        askYesOrNoFromInlineKeyboard(question, Callbacks.SETTINGS_CUSTOM_PHRASE_FINISH, Callbacks.CANCEL);
    }
}
