package dev.sorokin.eventmanager.Error;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ErrorDto(
        String message,
        String detailedMessage,
        LocalDateTime localDateTime
) {
}
