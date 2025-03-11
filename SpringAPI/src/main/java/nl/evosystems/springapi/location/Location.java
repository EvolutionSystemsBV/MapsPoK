package nl.evosystems.springapi.location;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.evosystems.springapi.common.BaseEntity;
import nl.evosystems.springapi.panels.LocationPanels;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Location  extends BaseEntity {
    private String city;
    private String street;
    private Integer number;
    private String addition;
    private String state;
    private String zip;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "location")
    private List<LocationPanels> panels;
}
