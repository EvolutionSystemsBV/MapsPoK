package nl.evosystems.springapi.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findByZipAndNumber(@Param("zip") String zip, @Param("number") Integer number);
}
