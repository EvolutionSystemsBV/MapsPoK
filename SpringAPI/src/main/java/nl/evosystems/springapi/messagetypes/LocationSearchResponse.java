package nl.evosystems.springapi.messagetypes;

public record LocationSearchResponse(
        String street,
        Integer number,
        String city,
        String zip,
        String state,
        Double latitude,
        Double longitude
) {
}
