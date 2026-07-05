package dev.sorokin.eventmanager.events.api;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public record EventDto(
        Long id,

        @NotBlank(message = "имя должно быть обязательно ")
        String name,

        Long ownerId,

        Integer maxPlaces,

        Integer occupiedPlaces,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId,

        String status
) {


}
