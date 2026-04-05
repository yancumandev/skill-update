package dev.sorokin.eventmanager.users;

import jakarta.validation.constraints.NotBlank;

public record SingInRequest(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
