package nl.evosystems.springapi.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.evosystems.springapi.ClientHostResolver;
import nl.evosystems.springapi.email.EmailService;
import nl.evosystems.springapi.email.EmailTemplateName;
import nl.evosystems.springapi.messagetypes.LoginRequest;
import nl.evosystems.springapi.messagetypes.LoginResponse;
import nl.evosystems.springapi.role.RoleRepository;
import nl.evosystems.springapi.session.SessionRepository;
import nl.evosystems.springapi.session.Session;
import nl.evosystems.springapi.user.User;
import nl.evosystems.springapi.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.email.confirmation-url}")
    private String confirmationUrl;
    @Value("${app.server.name}")
    private String serverName;

    public void register(RegistrationRequest request) throws IllegalStateException, MessagingException {
        // get user role
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role User was not found"));
        // build new user
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isLocked(false)
                .isActive(false)
                .isVerified(false)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
        // save new user
        User savedUser = userRepository.save(user);
        // send verification email
        sendVerificationEmail(savedUser);
    }

    private void sendVerificationEmail(User user) throws IllegalStateException, MessagingException {
        System.out.println("Sending verification email for user: " + user.getId());
        // create and save token
        String allowedCharacters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
        }
        String generatedToken = codeBuilder.toString();
        user.setVerificationToken(generatedToken);
        userRepository.save(user);
        // send email
        emailService.sendEmail(user.getEmail(), user.getFullName(), EmailTemplateName.ACTIVATE_ACCOUNT, confirmationUrl, generatedToken, "Account Activation");
    }

    public void generateNewVerificationCode(String userEmail) throws IllegalStateException, MessagingException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalStateException("User not found"));
        sendVerificationEmail(user);
    }

    public LoginResponse login(@Valid LoginRequest loginRequest, HttpServletRequest request) throws AuthenticationException {
        var aut = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = (User)aut.getPrincipal();
        System.out.println("User: " + user.getId());
        claims.put("fullName", user.getFullName());
        var token = jwtService.generateToken(claims, user);
        // get the user IP
        String ip = new ClientHostResolver(request).resolve();
        // create session
        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setIpAddress(ip);
        session.setLanguage("en");
        session.setSessionServerName(serverName);
        Session savedSession = sessionRepository.save(session);
        return new LoginResponse(true, null, savedSession);
    }

    @Transactional
    public void activateUser(String userEmail, String code) throws IllegalStateException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalStateException("User not found"));
        if (!user.getVerificationToken().equals(code)) { throw new IllegalStateException("Invalid verification code");}
        user.setIsVerified(true);
        user.setIsActive(true);
        userRepository.save(user);
    }
}
