package nl.evosystems.springapi.geolocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocationRepository extends JpaRepository<GeoLocation, String> {
}
