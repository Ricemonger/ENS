package app.telegram.bot.commands;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
public class BotCommandsConfig {

    public static final String SEND_HELP_MESSAGE = """
                        Send methods:
                            Send to one contact, not necessary saved, by:
                                method + contact identifier + notification text
                            Send to many, already saved, contacts, by filters:
                                contacting method[SMS,VIBER,EMAIL,TELEGRAM]
                                method + contact identifier(partial)
                                notification template name(partial)
            """;

    public static final String TASK_HELP_MESSAGE = """
            Tasks methods:
                Show all my tasks
                Add task for one not saved before contact
                Add task for many of yours saved contacts by contact pattern
                Add task for ALL contacts on account
                Delete one task by name
                Delete all tasks
            """;

    public static final String CONTACT_HELP_MESSAGE = """
            Contact methods:
               Show all my contacts
               Add one contact
               Add multiple contacts
               Delete one contact
               Delete multiple contacts by following filters:
                   contacting method[SMS,VIBER,EMAIL,TELEGRAM]
                   method + contact identifier(partial)
                   notification template name(partial)
            """;

    public static final String NOTIFICATION_HELP_MESSAGE = """
            Notification methods:
               Show all my notifications
               Add one notification template
               Add multiple notification templates
               Delete one notification template
               Delete multiple notification templates by following filters:
                   notification template name(partial)
                   notification template text(partial)
            """;

    public static final String DATA_HELP_MESSAGE = """
            Data methods:
               Show me all my data
               Remove ALL my data, including my account's entry in bot's database
            """;

    public static final String SETTINGS_HELP_MESSAGE = """
            Settings:
                Turn ON/OFF action confirmation for sendAll operation
                Create custom phrase for sendAll operation
            """;

    public static final String HELP_MESSAGE = "/start - Start communication with bot and register in bot's database\n" +
            "/send - Send notification to one or many chosen emergency contacts\n" +
            "/sendall - Requires action confirmation! Send notification to ALL your emergency contacts\n" +
            "Also available by typing \"send all\", \"sendall\" or your custom phrase to bot\n" +
            "Custom phrase and action confirmation requirement can be configured in settings\n" +
            "/task - Interaction with tasks(delayed notifications with timer for sending)" +
            TASK_HELP_MESSAGE +
            "/contact - Get access to contacts related methods:\n" +
            CONTACT_HELP_MESSAGE +
            "/notification - Get access to notifications related methods:\n" +
            NOTIFICATION_HELP_MESSAGE +
            "/data - Get access to data related methods:\n" +
            DATA_HELP_MESSAGE +
            "/clear - Remove all your contacts and notifications\n" +
            "/link - Link your telegram to existing ENS account. Unlink if linked already\n" +
            "/settings - Get and change your account related settings\n" +
            SETTINGS_HELP_MESSAGE;

    private final List<BotCommand> publicCommands = new ArrayList<>();

    {
        publicCommands.add(new BotCommand("/start", "Start communication with bot"));
        publicCommands.add(new BotCommand("/send", "Send notification to chosen emergency contact(s)"));
        publicCommands.add(new BotCommand("/sendall", "Send notifications to ALL emergency contacts"));
        publicCommands.add(new BotCommand("/help", "Expanded description of all commands"));
        publicCommands.add(new BotCommand("/task", "Interaction with tasks(delayed notifications)"));
        publicCommands.add(new BotCommand("/contact", "Interactions with emergency contacts"));
        publicCommands.add(new BotCommand("/notification", "Interactions with notifications' templates"));
        publicCommands.add(new BotCommand("/data", "Interaction with data in bot's database"));
        publicCommands.add(new BotCommand("/clear", "Remove all emergency contacts & notifications' templates"));
        publicCommands.add(new BotCommand("/link", "(Un)Link telegram with existing ENS account"));
        publicCommands.add(new BotCommand("/settings", "Account related settings"));
    }
}
