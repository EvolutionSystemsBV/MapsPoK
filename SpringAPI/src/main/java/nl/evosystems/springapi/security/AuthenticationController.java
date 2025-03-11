package nl.evosystems.springapi.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nl.evosystems.springapi.messagetypes.ErrorResponse;
import nl.evosystems.springapi.messagetypes.LoginRequest;
import nl.evosystems.springapi.messagetypes.LoginResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IllegalStateException", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "AuthenticationException", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequest request) throws IllegalStateException, MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/new-verification-code/{email}")
    public void requestNewVerificationCode(@PathVariable("email") String userEmail) throws IllegalStateException, MessagingException {
        service.generateNewVerificationCode(userEmail);
    }

    @GetMapping("/activate-user/{email}/{code}")
    public void activateUser(@PathVariable("email") String userEmail, @PathVariable("code") String code) throws IllegalStateException {
        service.activateUser(userEmail, code);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest body, HttpServletRequest request) throws AuthenticationException {
        return ResponseEntity.ok(service.login(body, request));
    }

}
