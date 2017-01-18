package com.pengelkes.properties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * Created by Pattis-PC on 06.01.2017.
 */
@ConfigurationProperties(prefix = "environment")
@Component
open class DatabaseProperties @Autowired constructor(private val environment: Environment) {
    var url: String? = null
    var user: String? = null
    var password: String? = null

    fun getDatabaseUrl(): String = environment.getProperty(url)
    fun getDatabaseUser(): String = environment.getProperty(user)
    fun getDatabasePassword(): String = environment.getProperty(password)
}
