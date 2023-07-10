package app.boot.mainapp.sms.dto;

public record SmsRequest(
        String phoneNumber,
        String message
) {
}
