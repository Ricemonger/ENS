package app.contact.model.db;

import app.utils.feign_clients.contact.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactCompositeKey implements Serializable {
    private String accountId;
    private Method method;
    private String contactId;
}
