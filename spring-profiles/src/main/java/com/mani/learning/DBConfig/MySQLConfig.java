package com.mani.learning.DBConfig;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev")
@Profile({"dev", "qa"})
public class MySQLConfig {

    public MySQLConfig() {
        System.out.println("MySQLConfig is created");
    }

    public void init() {
        System.out.println("MySQLConfig is initialized");
    }

}
