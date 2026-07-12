package com.mani.learning;

import com.mani.learning.DBConfig.MySQLConfig;
import com.mani.learning.DBConfig.NoSQLConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component

public class Profiles {

    @Value("${db-username}")
    private String userName;

    @Value("${db-password}")
    private String password;

    @Autowired(required = false)
    private MySQLConfig mySQLConfig;

    @Autowired(required = false)
    private NoSQLConfig noSQLConfig;

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
        System.out.println("mySQLConfig: " + mySQLConfig);
        System.out.println("noSQLConfig: " + noSQLConfig);
        System.out.println("\n=== DEBUG INFO ===");
        System.out.println("Active Profiles: " + java.util.Arrays.toString(environment.getActiveProfiles()));
        System.out.println("OS USERNAME env var: " + System.getenv("USERNAME"));
        System.out.println("username property from environment: " + environment.getProperty("db-username"));
        System.out.println("password property from environment: " + environment.getProperty("db-password"));
        System.out.println("==================\n");
    }
}
