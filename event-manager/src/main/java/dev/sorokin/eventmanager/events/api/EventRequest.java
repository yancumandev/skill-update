package dev.sorokin.eventmanager.events.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EventRequest(
        // Название мероприятия
        String name,

        //сколько мест
        @Positive(message = "должно быть больше целого числа")
        Integer maxPlaces,

        // дата
        LocalDate date,

        //цена
        @Min(value = 1, message = "минимальное цена от 1 и выше")
        Integer cost,

        //длительность
        @Min(value = 30, message = "длительность от 30 минут")
        Integer duration,

        // айди локации указывается организатором
        @NotNull
        Long locationId
) {
}
