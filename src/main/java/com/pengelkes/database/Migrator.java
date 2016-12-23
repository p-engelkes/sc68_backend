package com.pengelkes.database;

import org.flywaydb.core.Flyway;

/**
 * Created by pengelkes on 02.12.2016.
 */
public class Migrator
{
    public static void main(String[] args)
    {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://" + "localhost" + ":" + "5433" + "/" + "sc68",
                "lagoon",
                "lagoon"
        );
        flyway.setLocations("db/migrations");
        flyway.migrate();
    }
}
