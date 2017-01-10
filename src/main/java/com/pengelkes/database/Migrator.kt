package com.pengelkes.database

import org.flywaydb.core.Flyway

/**
 * Created by pengelkes on 02.12.2016.
 */
object Migrator {
    @JvmStatic fun main(args: Array<String>) {
        val flyway = Flyway()
        flyway.setDataSource("jdbc:postgresql://" + "localhost" + ":" + "5433" + "/" + "sc68_test",
                "lagoon",
                "lagoon"
        )
        flyway.setLocations("db/migrations")
        flyway.clean()
    }
}
