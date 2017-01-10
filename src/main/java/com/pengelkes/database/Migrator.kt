package com.pengelkes.database

import com.pengelkes.properties.DatabaseProperties
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by pengelkes on 02.12.2016.
 */
@Component
open class Migrator @Autowired constructor(private val databaseProperties: DatabaseProperties) {

    companion object {
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

    fun cleanAndMigrate() {
        val flyway = Flyway()
        flyway.setDataSource(databaseProperties.url,
                databaseProperties.user,
                databaseProperties.password)

        flyway.setLocations("db/migrations")
        flyway.clean()
        flyway.migrate()
    }
}
