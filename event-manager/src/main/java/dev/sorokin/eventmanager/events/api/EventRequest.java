package dev.sorokin.eventmanager.events.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventRequest(

        @NotBlank
        String name,

        @Positive(message = "Должно быть больше целого числа")
        @Min(value = 5, message = "минимум 5")
        Integer maxPlaces,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @Future(message = "Date must be in future")
        LocalDateTime date,

        @NotNull
        @Min(value = 10, message = "минимальное цена от 10 и выше")
        Integer cost,

        @NotNull
        @Min(value = 30, message = "длительность от 30 минут")
        Integer duration,

        @NotNull
        Long locationId
) {
}
