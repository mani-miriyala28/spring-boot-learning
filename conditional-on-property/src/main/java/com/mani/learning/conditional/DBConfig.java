package com.mani.learning.conditional;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBConfig {

    @Autowired(required = false)
    private MySQLConnection mySQLConnection;

    @Autowired(required = false)
    private NoSQLConnection noSQLConnection;

    @PostConstruct
    public void init(){

        System.out.println("DBConfig is initialized");
        System.out.println("mySQLConnection: " + mySQLConnection);
        System.out.println("noSQLConnection: " + noSQLConnection);
    }

}
