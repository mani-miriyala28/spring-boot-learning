package com.mani.learning.conditional;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="nosqlconnection",
        value="enabled",
        havingValue="create",
        matchIfMissing = false)
public class NoSQLConnection {

    public NoSQLConnection() {
        System.out.println("NoSQL Connection is created");
    }
}
