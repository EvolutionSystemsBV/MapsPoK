package nl.evosystems.springapi;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequestScope
public class ClientHostResolver {

    private final HttpServletRequest request;

    @Autowired
    public ClientHostResolver(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Resolves client IP address when application is behind a NGINX or other reverse proxy server
     */
    public String resolve() {

        String xRealIp = request.getHeader("X-Real-IP"); // used by Nginx
        String xForwardedFor = request.getHeader("X-Forwarded-For"); // used by the majority of load balancers
        String remoteAddr = request.getRemoteAddr(); // otherwise uses the remote IP address obtained by our Servlet container

        // returns the first non null
        return Stream.of(xRealIp, xForwardedFor, remoteAddr)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
