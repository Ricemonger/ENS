package app.telegram.bot;

public class Callbacks {

    public static final String REGISTER_YES = "REGISTER_YES";
    public static final String REGISTER_NO = "REGISTER_NO";

    public static final String SEND_ONE = "SEND_ONE";
    public static final String SEND_ONE_FINISH = "SEND_ONE_FINISH";

    public static final String SEND_MANY = "SEND_MANY";
    public static final String SEND_MANY_FINISH = "SEND_MANY_FINISH";

    public static final String SEND_ALL = "SEND_ALL";

    public static final String TASK_SHOW = "TASK_SHOW";

    public static final String TASK_CREATE = "TASK_CREATE";
    public static final String TASK_CREATE_FINISH = "TASK_CREATE_FINISH";

    public static final String TASK_DELETE = "TASK_DELETE";
    public static final String TASK_DELETE_FINISH = "TASK_DELETE_FINISH";

    public static final String TASK_DELETE_ALL = "TASK_DELETE_ALL";
    public static final String TASK_DELETE_ALL_FINISH = "TASK_DELETE_ALL_FINISH";

    public static final String CONTACT_SHOW = "CONTACT_SHOW";

    public static final String CONTACT_ADD = "CONTACT_ADD";
    public static final String CONTACT_ADD_FINISH = "CONTACT_ADD_FINISH";

    public static final String CONTACT_REMOVE_ONE = "CONTACT_REMOVE_ONE";
    public static final String CONTACT_REMOVE_ONE_FINISH = "CONTACT_REMOVE_ONE_FINISH";

    public static final String CONTACT_REMOVE_MANY = "CONTACT_REMOVE_MANY";
    public static final String CONTACT_REMOVE_MANY_FINISH = "CONTACT_REMOVE_MANY_FINISH";

    public static final String NOTIFICATION_SHOW = "NOTIFICATION_SHOW";

    public static final String NOTIFICATION_ADD = "NOTIFICATION_ADD";
    public static final String NOTIFICATION_ADD_FINISH = "NOTIFICATION_ADD_FINISH";

    public static final String NOTIFICATION_REMOVE_ONE = "NOTIFICATION_REMOVE_ONE";
    public static final String NOTIFICATION_REMOVE_ONE_FINISH = "NOTIFICATION_REMOVE_ONE_FINISH";

    public static final String NOTIFICATION_REMOVE_MANY = "NOTIFICATION_REMOVE_MANY";
    public static final String NOTIFICATION_REMOVE_MANY_FINISH = "NOTIFICATION_REMOVE_MANY_FINISH";

    public static final String DATA_SHOW = "DATA_SHOW";

    public static final String DATA_REMOVE = "DATA_REMOVE";
    public static final String DATA_REMOVE_FINISH = "DATA_REMOVE_FINISH";

    public static final String CLEAR = "CLEAR";

    public static final String LINK = "LINK";
    public static final String LINK_FINISH = "LINK_FINISH";

    public static final String UNLINK = "UNLINK";

    public static final String SETTINGS_ACTION_CONFIRMATION = "SETTINGS_ACTION_CONFIRMATION";
    public static final String SETTINGS_ACTION_CONFIRMATION_ENABLE = "SETTINGS_ACTION_CONFIRMATION_ENABLE";
    public static final String SETTINGS_ACTION_CONFIRMATION_DISABLE = "SETTINGS_ACTION_CONFIRMATION_DISABLE";

    public static final String SETTINGS_CUSTOM_PHRASE = "SETTINGS_CUSTOM_PHRASE";
    public static final String SETTINGS_CUSTOM_PHRASE_FINISH = "SETTINGS_CUSTOM_PHRASE_FINISH";

    public static final String CANCEL = "CANCEL";

    public static final String EMPTY = "EMPTY";

    public static final String METHOD_SMS = "METHOD_SMS";
    public static final String METHOD_VIBER = "METHOD_VIBER";
    public static final String METHOD_EMAIL = "METHOD_EMAIL";
    public static final String METHOD_TELEGRAM = "METHOD_TELEGRAM";

    public static final String TASK_TYPE_ONE = "TASK_TYPE_ONE";
    public static final String TASK_TYPE_MANY = "TASK_TYPE_MANY";
    public static final String TASK_TYPE_ALL = "TASK_TYPE_ALL";

    public static boolean isMethod(String data) {
        return data != null && (data.equals(METHOD_SMS) || data.equals(METHOD_VIBER) || data.equals(METHOD_EMAIL) || data.equals(METHOD_TELEGRAM));
    }

    public static boolean isTaskType(String data) {
        return data != null && (data.equals(TASK_TYPE_ONE) || data.equals(TASK_TYPE_MANY) || data.equals(TASK_TYPE_ALL));
    }
}
