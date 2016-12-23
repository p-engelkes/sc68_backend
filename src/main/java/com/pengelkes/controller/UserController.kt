package com.pengelkes.controller

import com.pengelkes.service.user.Position
import com.pengelkes.service.user.User
import com.pengelkes.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletException

/**
 * Created by pengelkes on 29.11.2016.
 */
@RestController
@RequestMapping("/api")
class UserController
@Autowired
constructor(private val userService: UserService) {

    @RequestMapping(value = "/user/register", method = arrayOf(RequestMethod.POST))
    @Throws(ServletException::class)
    fun registerUser(@RequestBody user: User): Int {
        return userService.registerNewUser(user)
    }

    @RequestMapping(value = "positions", method = arrayOf(RequestMethod.GET))
    fun getAllPositions(): List<Position> {
        return Position.values().asList()
    }

    @RequestMapping(value = "/user/{id}", method = arrayOf(RequestMethod.GET))
    fun getUser(@PathVariable id: Int): User? {
        return userService.findById(id)
    }

    @RequestMapping(value = "/user/{id}", method = arrayOf(RequestMethod.POST))
    fun updateUser(@RequestBody user: User, @PathVariable id: Int): User? {
        return userService.update(user)
    }
}
