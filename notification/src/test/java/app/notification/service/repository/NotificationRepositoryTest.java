package app.notification.service.repository;

import app.notification.model.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void findAllByUsername() {
        String username = "username";
        List<Notification> list = new ArrayList<>();
        List<Notification> result = new ArrayList<>();
        Notification not1 = new Notification(username, "name1", "text");
        Notification not2 = new Notification(username, "name4", "teerext");
        result.add(not1);
        result.add(not2);
        list.add(not1);
        list.add(not2);
        list.add(new Notification("user", "name2", "text1"));
        list.add(new Notification("username1", "name3", "te"));
        notificationRepository.saveAll(list);
        assertEquals(notificationRepository.findAllByUsername(username), result);
    }
}