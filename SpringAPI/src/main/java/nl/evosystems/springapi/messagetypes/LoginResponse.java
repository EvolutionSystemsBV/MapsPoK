package nl.evosystems.springapi.messagetypes;

import nl.evosystems.springapi.session.Session;

public record LoginResponse(Boolean success, String error, Session session) {
}
