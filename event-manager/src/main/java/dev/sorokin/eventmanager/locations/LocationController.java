package dev.sorokin.eventmanager.locations;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationDtoConvertor locationDtoConvertor;

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    public LocationController(LocationService locationService, LocationDtoConvertor locationDtoConvertor) {
        this.locationService = locationService;
        this.locationDtoConvertor = locationDtoConvertor;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto locationDto) {
        log.info("запрос на создание сущности {}", locationDto);
        var result = locationService.createLocation(locationDtoConvertor.toDomain(locationDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationDtoConvertor.toDto(result));
    }

    @GetMapping("{id}")
    public ResponseEntity<LocationDto> findById(@PathVariable("id") Long id) {
        log.info("поиск сущности с id = {}", id);
        var result = locationService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDtoConvertor.toDto(result));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> findAll() {
        log.info("поиск всех локаций");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationService.findAll().stream()
                        .map(locationDtoConvertor::toDto)
                        .toList()
                );
    }

    @DeleteMapping("{id}")
 //   @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        log.info("DEL сущности с id = {}", id);
        locationService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @Valid @RequestBody LocationDto locationDto,
            @PathVariable Long id) { // почему тут после паф варил нет смкобок
        log.info("UPDATE сущности с id = {}", locationDto);

        var result = locationService.updateLocations(locationDtoConvertor.toDomain(locationDto), id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationDtoConvertor.toDto(result));
    }


}

