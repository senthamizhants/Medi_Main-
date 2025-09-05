package com.example.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    public Location findById(long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public String getLocationNameById(Long id) {
        if (id == null) return "";
        return locationRepository.findById(id)
                .map(Location::getLocation)
                .orElse("Unknown");
    }

    // ✅ New method for VARCHAR-based lookup
    public String getLocationNameByCode(String locationCode) {
        if (locationCode == null || locationCode.isEmpty()) return "";
        
        Optional<Location> optionalLocation = locationRepository.findByLocation(locationCode);
        
        return optionalLocation.map(Location::getLocation).orElse("Unknown");
    }
}
