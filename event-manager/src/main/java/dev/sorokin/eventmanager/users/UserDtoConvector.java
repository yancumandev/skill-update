package dev.sorokin.eventmanager.users;

import org.springframework.stereotype.Component;

@Component
public class UserDtoConvector {

    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                String.valueOf(user.role())
        );
    }

}
