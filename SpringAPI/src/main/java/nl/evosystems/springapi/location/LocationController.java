package nl.evosystems.springapi.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.evosystems.springapi.common.PageResponse;
import nl.evosystems.springapi.messagetypes.ErrorResponse;
import nl.evosystems.springapi.messagetypes.LocationRequest;
import nl.evosystems.springapi.messagetypes.LocationSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("location")
@RequiredArgsConstructor
@Tag(name = "Location")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<String> createLocation(@Valid @RequestBody LocationRequest location) {
        return ResponseEntity.ok(locationService.create(location));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable String id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponse<Location>> getAllLocations(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
            ) {
        return ResponseEntity.ok(locationService.findAllLocations(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationSearchResponse>> searchLocations(@RequestParam String query) throws JsonProcessingException, IllegalStateException {
        return ResponseEntity.ok(locationService.search(query));
    }
}
