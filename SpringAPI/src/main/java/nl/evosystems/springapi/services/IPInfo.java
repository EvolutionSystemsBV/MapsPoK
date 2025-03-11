package nl.evosystems.springapi.services;

import org.springframework.stereotype.Service;

@Service
public class IPInfo {
    private String endpoint;

    public String getLocation(String ip) {
        return "Apeldoorn";
    }
}
