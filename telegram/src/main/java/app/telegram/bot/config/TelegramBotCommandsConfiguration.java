package app.telegram.bot.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
public class TelegramBotCommandsConfiguration {

    public static final String CONTACT_HELP_MESSAGE = """
            Contact methods:
               Add one contact
               Add multiple contacts
               Delete one contact
               Delete multiple contacts by following filters:
                   contacting method[SMS,VIBER,EMAIL,TELEGRAM]
                   notification template name(full or partial)
                   contact identifier(full or partial)
            """;

    public static final String NOTIFICATION_HELP_MESSAGE = """
            Notification methods:
               Add one notification template
               Add multiple notification templates
               Delete one notification template
               Delete multiple notification templates by following filters:
                   notification template name(full or partial)
                   notification template text(full or partial)
            """;

    public static final String DATA_HELP_MESSAGE = """
            Data methods:
               Show me all my data
               Remove ALL my data, including telegram account entry in bot's database
            """;

    public static final String HELP_MESSAGE = "/start - Start communication with bot and register in bot's database\n" +
            "/send - Send notification to chosen emergency contact\n" +
            "/sendall - Requires action confirmation! Send notification to ALL your emergency contacts\n" +
            "Also available by typing \"send\", \"send all\", \"sendall\" or your custom phrase to bot\n" +
            "Custom phrase and action confirmation requirement can be configured in settings\n" +
            "/contact - Get access to contacts related methods:\n" +
            CONTACT_HELP_MESSAGE +
            "/notification - Get access to notifications related methods:\n" +
            NOTIFICATION_HELP_MESSAGE +
            "/data - Get access to data related methods:\n" +
            DATA_HELP_MESSAGE +
            "/clear - Remove all your contacts and notifications\n" +
            "/link - Link your telegram to existing ENS account\n" +
            "/settings - Get and change your account related settings\n";

    private final List<BotCommand> publicCommands = new ArrayList<>();

    {
        publicCommands.add(new BotCommand("/start", "Start communication with bot"));
        publicCommands.add(new BotCommand("/send", "Send notification to chosen emergency contact"));
        publicCommands.add(new BotCommand("/sendall", "Send notifications to ALL emergency contacts"));
        publicCommands.add(new BotCommand("/help", "Expanded description of all commands"));
        publicCommands.add(new BotCommand("/contact", "Interactions with emergency contacts"));
        publicCommands.add(new BotCommand("/notification", "Interactions with notifications' templates"));
        publicCommands.add(new BotCommand("/data", "Interaction with data in bot's database"));
        publicCommands.add(new BotCommand("/clear", "Remove all emergency contacts & notifications' templates"));
        publicCommands.add(new BotCommand("/link", "Link telegram to existing ENS account"));
    }

    private final List<BotCommand> allCommands = new ArrayList<>();

    {
        allCommands.addAll(publicCommands);
    }
}
