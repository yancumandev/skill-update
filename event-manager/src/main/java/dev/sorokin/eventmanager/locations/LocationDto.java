package dev.sorokin.eventmanager.locations;

import jakarta.validation.constraints.*;

public record LocationDto(
        @Null
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String address,
        @NotNull
        @Min(value = 5, message = "минимальное значение больше 5")
        Integer capacity,

        String description
) {
}
