package nl.evosystems.springapi.panels;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.evosystems.springapi.common.BaseEntity;
import nl.evosystems.springapi.location.Location;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LocationPanels extends BaseEntity {

    private Integer amount;
    private Integer watts;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
