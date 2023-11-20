package app.test;

import app.contact.controller.ContactController;
import app.contact.controller.dto.ContactCreUpdRequest;
import app.utils.feign_clients.contact.Contact;
import app.utils.feign_clients.contact.ContactFeignClient;
import app.utils.feign_clients.contact.Method;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ContactConnectionTests.class)
@ComponentScan(basePackages = {
        "app.contact",
        "app.utils.feign_clients.contact"})
@EnableFeignClients(basePackages = {
        "app.utils.feign_clients.contact"})
public class ContactConnectionTests {

    // SECURITY, CONTACT AND API-GATEWAY MODULES MUST BE RUNNING

    private static final String TOKEN = "TOKEN";

    private static final Contact CONTACT = new Contact(Method.SMS, "CONTACT_ID", "NOTIFICATION_NAME");

    @MockBean
    private ContactController controller;

    @SpyBean
    private ContactFeignClient feignClient;

    @Test
    public void findOneByPrimaryKey() {
        feignClient.create(TOKEN, CONTACT);

        verify(controller).create(TOKEN, new ContactCreUpdRequest(
                CONTACT.getMethod().name(),
                CONTACT.getContactId(),
                CONTACT.getNotificationName()));
    }

}
