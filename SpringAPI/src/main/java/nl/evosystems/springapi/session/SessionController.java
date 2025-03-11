package nl.evosystems.springapi.session;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import nl.evosystems.springapi.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("session")
@AllArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @GetMapping("/id/{id}")
    public Session getSession(@PathVariable("id") String id) {
        return sessionRepository.getReferenceById(id);
    }

}
