@file:Suppress("SpringKotlinAutowiredMembers")

package com.pengelkes.controller

import com.pengelkes.service.UserService
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by pengelkes on 06.01.2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = arrayOf("classpath:test.properties"))
class UserControllerTest {

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
}
