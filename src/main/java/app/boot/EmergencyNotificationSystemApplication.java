package app.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmergencyNotificationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmergencyNotificationSystemApplication.class, args);
    }
    //TODO PROPER EXCEPTION HANDLING, LOGGING, TESTING, BULK SEND FOR MULTIPLE SENDINGS, DIVIDE INTO MICROSERVICES
    // username,password validation in client and on server
    // method validation in client
    // contactId validation both in client and server
    // create contact's creadteupdaterequest without notificationName
    // sender's exception handling both in client and server
}
