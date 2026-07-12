package com.mani.learning.DBConfig;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("qa")
@Profile("dev | qa | prod")
public class NoSQLConfig {

    public NoSQLConfig() {
        System.out.println("NoSQLConfig is created");
    }
    public void init() {
        System.out.println("NoSQLConfig is initialized");
    }
}
