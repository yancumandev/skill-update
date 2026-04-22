package dev.sorokin.eventmanager.users;

public record UserDto(
        Long id,
        String login,
        Integer age,
        String role
) {
}
