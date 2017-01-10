@file:Suppress("SpringKotlinAutowiredMembers")

package com.pengelkes.controller

import com.pengelkes.DatabaseTestCase
import com.pengelkes.service.User
import com.pengelkes.service.UserService
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 06.01.2017.
 */
class UserTest : DatabaseTestCase() {

    @Autowired
    lateinit var userService: UserService

    @Test
    fun findUser() {
        userService.registerNewUser(User("test@test.com", "test"))
        userService.findByEmail("test@test.com").should.not.be.`null`
    }

    @Test
    fun findAll() {
        userService.findAll().should.have.size.equal(0)
    }

    @Test
    fun createUser() {
        val size = userService.findAll().size
        userService.registerNewUser(User("test@test1.com", "test"))

        userService.findAll().should.have.size.equal(size + 1)
    }
}
