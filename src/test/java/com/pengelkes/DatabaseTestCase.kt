package com.pengelkes

import com.pengelkes.database.Migrator
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by pengelkes on 10.01.2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = arrayOf("classpath:test.properties"))
abstract class DatabaseTestCase {

    @Autowired
    lateinit var migrator: Migrator

    @Before
    fun setup() {
        migrator.cleanAndMigrate()
    }

}