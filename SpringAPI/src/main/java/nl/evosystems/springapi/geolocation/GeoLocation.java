package nl.evosystems.springapi.geolocation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class GeoLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String ipaddress;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String location;
    private String organisation;
    private String postalCode;
    private String timezone;
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;

    public GeoLocation() {
    }

    public GeoLocation(String ipaddress, String hostname, String city, String region, String country, String location, String organisation, String postalCode, String timezone) {
        this.ipaddress = ipaddress;
        this.hostname = hostname;
        this.city = city;
        this.region = region;
        this.country = country;
        this.location = location;
        this.organisation = organisation;
        this.postalCode = postalCode;
        this.timezone = timezone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
