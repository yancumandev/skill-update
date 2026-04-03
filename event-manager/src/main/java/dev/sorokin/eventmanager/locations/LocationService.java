package dev.sorokin.eventmanager.locations;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConvertor locationEntityConvertor;

    public LocationService(LocationRepository locationRepository, LocationEntityConvertor locationEntityConvertor) {
        this.locationRepository = locationRepository;
        this.locationEntityConvertor = locationEntityConvertor;
    }


    public Location createLocation(Location location) {

        var locationToSave = locationEntityConvertor.toEntity(location);
        var locationEntity = locationRepository.save(locationToSave);

        return locationEntityConvertor.toDomain(locationEntity);
    }


    public Location findById(Long id) {
        var foundLocation = locationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "сущность не найдена с id = %d".formatted(id)
        ));
        return locationEntityConvertor.toDomain(foundLocation);
    }


    public void deleteById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("сущность не найдена с id = %d".formatted(id));
        }
        locationRepository.deleteById(id);
    }


    public List<Location> findAll() {
        return locationRepository.findAll().stream()
                .map(locationEntityConvertor::toDomain)
                .toList();
    }


    @Transactional
    public Location updateLocations(Location domain, Long id) {

        LocationEntity location = locationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "сущность не найдена с id = %d".formatted(id)
        ));

        location.setName(domain.name());
        location.setAddress(domain.address());
        location.setCapacity(domain.capacity());
        location.setDescription(domain.description());

        var saveLocation = locationRepository.save(location);
        return locationEntityConvertor.toDomain(saveLocation);
    }

}
