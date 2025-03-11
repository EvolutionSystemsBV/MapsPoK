package nl.evosystems.springapi.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.evosystems.springapi.messagetypes.LocationRequest;
import nl.evosystems.springapi.messagetypes.LocationSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import nl.evosystems.springapi.common.PageResponse;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    @Value("${app.nominatim-endpoint}")
    private String NOMINATIM_ENDPOINT;

    public String create(LocationRequest req) {
        Location loc = Location.builder()
                .id(req.id())
                .street(req.streetname())
                .number(req.housnumber())
                .addition(req.addition())
                .zip(req.postalCode())
                .city(req.city())
                .latitude(req.lattitude())
                .longitude(req.longitude())
                .build();
        return locationRepository.save(loc).getId();
    }

    public Location getLocationById(String id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location getLocationByZipAndNumber(String zip, Integer number) {
        return locationRepository.findByZipAndNumber(zip, number).orElseThrow( () -> new IllegalStateException("Location not found for zip " + zip + " and number " + number));
    }

    public PageResponse<Location> findAllLocations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "street", "number"));
        Page<Location> locations = locationRepository.findAll(pageable);
        return new PageResponse<>(
                locations.stream().toList(),
                locations.getNumber(),
                locations.getSize(),
                locations.getTotalPages(),
                locations.getTotalElements(),
                locations.isFirst(),
                locations.isLast());
    }


    public List<LocationSearchResponse> search(String query) throws JsonProcessingException, IllegalStateException {
        RestTemplate nominatimRestTemplate = new RestTemplate();
        String url = NOMINATIM_ENDPOINT.replace("{query}", query).replace("{limit}", "10");
        nominatimRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("User-Agent", "MySpringBootApp/1.0");
            return execution.execute(request, body);
        });
        String response = nominatimRestTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(response);
        List<LocationSearchResponse> searchResults = new ArrayList<>();
        for (JsonNode node : jsonArray) {
            JsonNode addressNode = node.get("address");
            var city = addressNode.hasNonNull("city") ? addressNode.get("city").asText() : null;
            if (city == null) { city = addressNode.hasNonNull("town") ? addressNode.get("town").asText() : null; }
            if (city == null) { city = addressNode.hasNonNull("village") ? addressNode.get("village").asText() : null; }
            if (city == null) { city = addressNode.hasNonNull("municipality") ? addressNode.get("municipality").asText() : null; }
            searchResults.add(new LocationSearchResponse(
                    addressNode.hasNonNull("road") ? addressNode.get("road").asText() : null,
                    addressNode.hasNonNull("house_number") ? addressNode.get("house_number").asInt() : null,
                    city,
                    addressNode.hasNonNull("postcode") ? addressNode.get("postcode").asText() : null,
                    addressNode.get("state").asText(),
                    node.get("lat").asDouble(),
                    node.get("lon").asDouble()

            ));
        }
        if (searchResults.isEmpty()) {
            throw new IllegalStateException("No cities found in the Netherlands matching '" + query + "'");
        }
        return searchResults;
    }
}
