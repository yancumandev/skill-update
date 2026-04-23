package dev.sorokin.eventmanager.events.api;

public record EventDto(
        Long id,

        //название мероприятия
        String name,

        //id пользователя-создателя мероприятия
        Long ownerId,

        //Максимальная вместиость
        Integer maxPlaces,

        //Кол-во уже занятых мест (создатель не учитывается при подсчете)
        Integer occupiedPlaces,

        //ата и время проведения мероприятия. Формат "YYYY-MM-DDThh:mm:ss"
        String date,

        //Стоимость в рублях
        Integer cost,

        // Длительность в минутах
        Integer duration,

        //Идентификатор локации, где проходит мероприятие
        Integer locationId,

        // статус
        String status
) {


}
