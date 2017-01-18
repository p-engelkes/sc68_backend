package com.pengelkes.properties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * Created by Pattis-PC on 06.01.2017.
 */
@ConfigurationProperties(prefix = "database")
@Component
open class DatabaseProperties @Autowired constructor(private val environment: Environment) {
    var environmentUrl: String? = null

    fun getDatabaseConnectionString(): String = environment.getProperty(environmentUrl)

    fun getUser(): String {
        var connectionString = getDatabaseConnectionString()
        connectionString = connectionString.substring(11, connectionString.length)
        val user = connectionString.substringBefore(':')
        return user
    }

    fun getPassword(): String {
        var connectionString = getDatabaseConnectionString()
        connectionString = connectionString.substring(11, connectionString.length)
        val userAndPassword = connectionString.substringBefore('@')
        val password = userAndPassword.substringAfter(":")
        return password
    }

    fun getUrl(): String {
        var connectionString = getDatabaseConnectionString()
        connectionString = "jdbc:" + connectionString
        connectionString = connectionString.replace("postgres", "postgresql")
        val userAndPassword = getUser() + ":" + getPassword() + "@"
        val url = connectionString.replace(userAndPassword, "")
        return url
    }
}
