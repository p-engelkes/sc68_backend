package com.pengelkes.controller

import com.pengelkes.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.ServletException

/**
 * Created by pengelkes on 30.11.2016.
 */
@RestController
@RequestMapping("/api/security")
class SecurityController
@Autowired
constructor(private val userService: UserService) {

    @RequestMapping(value = "/verifyLogin/{email:.+}")
    @Throws(ServletException::class)
    fun verifyToken(@PathVariable email: String): Int {
        userService.findByEmail(email)?.let {
            return it.id
        }

        throw ServletException("Ein Benutzer mit dieser Adresse existiert nicht")
    }

}
