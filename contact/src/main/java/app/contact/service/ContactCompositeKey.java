package app.contact.service;

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
