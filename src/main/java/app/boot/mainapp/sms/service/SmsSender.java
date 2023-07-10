package app.boot.mainapp.sms.service;

import app.boot.mainapp.sms.dto.SmsRequest;

public interface SmsSender {
    void sendSMS(SmsRequest smsRequest);
}
