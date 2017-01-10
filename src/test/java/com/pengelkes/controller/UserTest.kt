@file:Suppress("SpringKotlinAutowiredMembers")

package com.pengelkes.controller

import com.pengelkes.service.User
import com.pengelkes.service.UserService
import org.flywaydb.test.annotation.FlywayTest
import org.flywaydb.test.junit.FlywayTestExecutionListener
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import kotlin.test.assertEquals

/**
 * Created by pengelkes on 06.01.2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = arrayOf("classpath:test.properties"))
@TestExecutionListeners(listeners = arrayOf(DependencyInjectionTestExecutionListener::class,
        FlywayTestExecutionListener::class))
class UserTest {

    @Autowired
    lateinit var userService: UserService

    @Test
    @FlywayTest
    fun findUser() {
        userService.registerNewUser(User("test@test.com", "test"))
        assertTrue(userService.findByEmail("test@test.com") != null)
    }

    @Test
    @FlywayTest
    fun findAll() {
        assertEquals(0, userService.findAll().size)
    }

    @Test
    @FlywayTest
    fun createUser() {
        val size = userService.findAll().size
        userService.registerNewUser(User("test@test1.com", "test"))

        assertEquals(size + 1, userService.findAll().size)
    }
}
