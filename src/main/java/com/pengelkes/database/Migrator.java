package com.pengelkes.database;

import org.flywaydb.core.Flyway;

import java.util.Properties;

/**
 * Created by pengelkes on 02.12.2016.
 */
public class Migrator
{
    public static void main(String[] args)
    {
        Flyway flyway = new Flyway();
        Properties properties = new Properties();
        properties.setProperty("flyway.user", "lagoon");
        properties.setProperty("flyway.password", "lagoon");
        properties.setProperty("flyway.url", "jdbc:postgresql://" + "localhost" + ":" + "5433" + "/" + "sc68");
        flyway.configure(properties);
        flyway.setLocations("db/migration");
        flyway.migrate();
    }
}
