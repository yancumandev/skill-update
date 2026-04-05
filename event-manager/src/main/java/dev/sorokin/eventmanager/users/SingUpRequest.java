package dev.sorokin.eventmanager.users;

import jakarta.validation.constraints.*;

public record SingUpRequest(
        @NotBlank
        @Size(min = 5, message = "Логин должен быть больше 5 символов")
        String login,
        @NotBlank
        @Size(min = 5, message = "Пароль должен быть больше 5 символов")
        String password,
        @NotNull
        @Min(value = 18, message = "Возраст должен от 18 (включительно) ")
        Integer age
) {
}
