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

    companion object {
        val updatedFirstName = "Peter"
        val updatedLastName = "Pan"
        val updatedBacknumber = 8
        val userMail = "test@test.com"
        val user = User(userMail, "test")
    }

    @Autowired
    lateinit var userService: UserService

    @Test
    fun testRegisterNewUser() {
        val size = userService.findAll().size
        userService.registerNewUser(user)

        userService.findAll().should.have.size.equal(size + 1)
    }

    @Test
    fun findAll() {
        userService.findAll().should.have.size.equal(0)
    }

    @Test
    fun findByEmail() {
        userService.registerNewUser(user)
        userService.findByEmail(userMail).should.not.be.`null`
    }

    @Test
    fun update() {
        userService.registerNewUser(user)
        var user = userService.findByEmail(userMail)
        if (user != null) {
            user.backNumber = updatedBacknumber
            user.firstName = updatedFirstName
            user.lastName = updatedLastName
            userService.update(user)
            user = userService.findByEmail(userMail)
            if (user != null) {
                user.backNumber.should.equal(updatedBacknumber)
                user.firstName.should.equal(updatedFirstName)
                user.lastName.should.equal(updatedLastName)
            } else {
                true.should.be.`false`
            }
        } else {
            true.should.be.`false`
        }

    }
}
