package com.mani.learning.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="mysqlconnection",
        value="enabled",
        havingValue="true",
        matchIfMissing = false)

public class MySQLConnection {

    public MySQLConnection() {
        System.out.println("MySQL Connection is created");
    }

}
