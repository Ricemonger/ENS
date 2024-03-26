package app.telegram.bot.commands.settings;

import app.telegram.bot.BotService;
import app.telegram.bot.Callbacks;
import app.telegram.bot.commands.AbstractBotCommand;
import app.telegram.bot.commands.BotCommandsConfig;
import app.telegram.bot.commands.CallbackButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SettingsDirect extends AbstractBotCommand {

    public SettingsDirect(TelegramLongPollingBot bot, Update update, BotService botService) {
        super(bot, update, botService);
    }

    @Override
    protected void executeCommand() {
        MyFunctionalInterface function = () -> {
            CallbackButton ac = new CallbackButton("SEND ALL ACTION CONFIRMATION",
                    Callbacks.SETTINGS_ACTION_CONFIRMATION);
            CallbackButton cp = new CallbackButton("SEND ALL CUSTOM PHRASE", Callbacks.SETTINGS_CUSTOM_PHRASE);

            boolean flag = botService.getUserActionConfirmFlag(chatId);
            String phrase = botService.getUserCustomPhrase(chatId);

            String questionAddition = String.format("Action confirmation flag: %s\nCustom phrase: %s", flag, phrase);

            askFromInlineKeyboard(BotCommandsConfig.SETTINGS_HELP_MESSAGE + questionAddition, 1, ac, cp);
        };
        executeCommandIfUserExistsOrAskToRegister(function);
    }
}
