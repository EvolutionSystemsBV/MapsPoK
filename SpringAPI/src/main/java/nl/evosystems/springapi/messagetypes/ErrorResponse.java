package nl.evosystems.springapi.messagetypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ErrorResponse {
    private int status;
    private String error;
    private String message;

}
