@file:Suppress("SpringKotlinAutowiredMembers")

package com.pengelkes.controller

import com.pengelkes.properties.DatabaseProperties
import com.pengelkes.service.User
import com.pengelkes.service.UserService
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

/**
 * Created by pengelkes on 06.01.2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = arrayOf("classpath:test.properties"))
class UserControllerTest() {

    @Autowired
    lateinit var databaseProperties: DatabaseProperties

    @Autowired
    lateinit var userService: UserService

    @Test
    fun helloUserService() {
        assertFalse(userService.helloIntegrationTest().isEmpty())
    }

    @Test
    fun findUser() {
        assertTrue(userService.findById(1) != null)
    }

    @Test
    fun findAll() {
        assertEquals(0, userService.findAll().size)
    }

    @Test
    fun createUser() {
        val size = userService.findAll().size
        userService.registerNewUser(User("test@test.com", "test"))

        assertEquals(size + 1, userService.findAll().size)
    }
}
