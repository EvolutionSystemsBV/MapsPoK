package nl.evosystems.springapi.messagetypes;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "Email is mandatory") String email,
        @NotEmpty(message = "Password is mandatory") String password) {
}
