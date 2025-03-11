package nl.evosystems.springapi.messagetypes;

public record RegisterRequest(String firstName, String lastName, String email, String password) {
}
