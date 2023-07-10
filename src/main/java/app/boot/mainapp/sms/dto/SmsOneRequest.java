package app.boot.mainapp.sms.dto;

public record SmsOneRequest(
        String phoneNumber,
        String notificationName
) {
}
