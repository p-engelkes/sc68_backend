@file:Suppress("SpringKotlinAutowiredMembers")

package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 06.01.2017.
 */
class UserTest : SpringTestCase() {

    companion object {
        val updatedFirstName = "Peter"
        val updatedLastName = "Pan"
        val updatedBacknumber = 8
        val testMail = "test@test.com"
        val userMail = "register@new.user"
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
        userService.findAll().should.have.size.equal(1)
    }

    @Test
    fun findByEmail() {
        userService.findByEmail(testMail).should.not.be.`null`
    }

    @Test
    fun update() {
        var user = userService.findByEmail(testMail)
        if (user != null) {
            user.backNumber = updatedBacknumber
            user.firstName = updatedFirstName
            user.lastName = updatedLastName
            userService.update(user)
            user = userService.findByEmail(testMail)
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
