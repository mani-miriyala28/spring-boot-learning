package com.mani.learning;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component

public class Profiles {

    @Value("${username}")
    private String userName;

    @Value("${password}")
    private String password;

    private final Environment environment;

    public Profiles(Environment environment) {
        System.out.println("Profiles bean is created");
        this.environment = environment;
    }

    @PostConstruct
    public void init(){
        System.out.println("Profiles bean is initialized");
        System.out.println("username: " + userName);
        System.out.println("password: " + password);
        System.out.println("\n=== DEBUG INFO ===");
        System.out.println("Active Profiles: " + java.util.Arrays.toString(environment.getActiveProfiles()));
        System.out.println("OS USERNAME env var: " + System.getenv("USERNAME"));
        System.out.println("username property from environment: " + environment.getProperty("username"));
        System.out.println("==================\n");
    }
}
