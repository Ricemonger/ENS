package app.security.user.service.any_user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbstractUser {
    private String accountId;
}
