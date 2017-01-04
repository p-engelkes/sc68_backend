package com.pengelkes.controller

import com.pengelkes.service.Position
import com.pengelkes.service.ProfilePictureService
import com.pengelkes.service.User
import com.pengelkes.service.UserService
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
constructor(private val userService: UserService,
            private val profilePictureService: ProfilePictureService) {

    @RequestMapping(value = "/users/register", method = arrayOf(RequestMethod.POST))
    @Throws(ServletException::class)
    fun registerUser(@RequestBody user: User) = userService.registerNewUser(user)

    @RequestMapping(value = "positions", method = arrayOf(RequestMethod.GET))
    fun getAllPositions() = Position.values().asList()

    @RequestMapping(value = "/users/{id}", method = arrayOf(RequestMethod.GET))
    fun getUser(@PathVariable id: Int) = userService.findById(id)

    @RequestMapping(value = "/users/{id}", method = arrayOf(RequestMethod.POST))
    fun updateUser(@RequestBody user: User, @PathVariable id: Int) = userService.update(user)
}
