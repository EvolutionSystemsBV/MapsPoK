package nl.evosystems.springapi.messagetypes;

public record LocationRequest(
    String id,
    String streetname,
    Integer housnumber,
    String addition,
    String city,
    String postalCode,
    Double lattitude,
    Double longitude
    ){}
