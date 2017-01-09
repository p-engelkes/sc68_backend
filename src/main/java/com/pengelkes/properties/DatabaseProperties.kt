package com.pengelkes.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Created by Pattis-PC on 06.01.2017.
 */
@ConfigurationProperties(prefix = "database")
@Component
open class DatabaseProperties {
    var url: String? = null
    var user: String? = null
    var password: String? = null
}
