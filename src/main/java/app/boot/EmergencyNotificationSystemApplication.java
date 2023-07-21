package app.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmergencyNotificationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmergencyNotificationSystemApplication.class, args);
    }
    //TODO PROPER EXCEPTION HANDLING, LOGGING, TESTING, BULK SEND FOR MULTIPLE SENDINGS, DIVIDE INTO MICROSERVICES
    // method validation in client
    // contactId validation both in client and server
}
