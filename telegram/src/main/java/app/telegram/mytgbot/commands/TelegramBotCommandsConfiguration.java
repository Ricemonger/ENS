package app.telegram.mytgbot.commands;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
public class TelegramBotCommandsConfiguration {

    public static final String HELP_MESSAGE = "HELP_MESSAGE";

    public static final String CONTACT_HELP_MESSAGE = "CONTACT_HELP_MESSAGE";

    public static final String NOTIFICATION_HELP_MESSAGE = "NOTIFICATION_HELP_MESSAGE";

    public static final String DATA_HELP_MESSAGE = "DATA_HELP_MESSAGE";

    private final List<BotCommand> publicCommands = new ArrayList<>();

    {
        publicCommands.add(new BotCommand("/start", "Start communication with bot"));
        publicCommands.add(new BotCommand("/send", "Send notification to chosen emergency contact"));
        publicCommands.add(new BotCommand("/sendall", "Send notifications to ALL emergency contacts"));
        publicCommands.add(new BotCommand("/help", "Expanded description of all commands"));
        publicCommands.add(new BotCommand("/contact", "Interactions with emergency contacts"));
        publicCommands.add(new BotCommand("/notification", "Interactions with notifications' templates"));
        publicCommands.add(new BotCommand("/clear", "Remove all emergency contacts & notifications' templates"));
        publicCommands.add(new BotCommand("/link", "Link telegram to existing ENS account"));
        publicCommands.add(new BotCommand("/data", "Interaction with data in bot's database"));
    }

    private final List<BotCommand> allCommands = new ArrayList<>();

    {
        allCommands.addAll(publicCommands);
    }
}
