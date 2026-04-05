package dev.sorokin.eventmanager.users;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void defaultAdmin() {
        createDefaultAdmin("admin", "admin", UserRole.ADMIN);
    }

    private void createDefaultAdmin(
            String login,
            String password,
            UserRole role
    ) {
        if (userService.isUserExistsByLogin(login)) {
            return;
        }
        var hashedPass = passwordEncoder.encode(password);
        var admin = new User(
                null,
                login,
                21,
                role,
                hashedPass
        );
        userService.initDefaultAdmin(admin);
    }


}
