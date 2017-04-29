package com.pengelkes.controller

import com.pengelkes.service.Position
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
constructor(private val userService: UserService) {

    @RequestMapping(value = "/users/register", method = arrayOf(RequestMethod.POST))
    @Throws(ServletException::class)
    fun registerUser(@RequestBody json: String): Int {
        User.fromJson(json)?.let {
            return userService.registerNewUser(it)
        }

        throw ServletException("Der Benutzer konnte nicht erstellt werden")
    }

    @RequestMapping(value = "positions", method = arrayOf(RequestMethod.GET))
    fun getAllPositions() = Position.values().map { it.translation }

    @RequestMapping(value = "/users/{id}", method = arrayOf(RequestMethod.GET))
    fun getUser(@PathVariable id: Int): User? {
        val user = userService.findById(id)
        user?.let {
            it.positionTranslation = it.position?.translation
        }

        return user;
    }

    @RequestMapping(value = "/users/{id}", method = arrayOf(RequestMethod.POST))
    fun updateUser(@RequestBody json: String, @PathVariable id: Int): User? {
        User.fromJson(json)?.let {
            return userService.update(it)
        }

        throw ServletException("Der Benutzer konnte nicht aktualisiert werden")
    }
}

