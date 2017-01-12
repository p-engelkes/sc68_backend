package com.pengelkes.controller

import com.pengelkes.service.Team
import com.pengelkes.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.ServletException

/**
 * Created by pengelkes on 02.12.2016.
 */
@RestController
@RequestMapping("/api")
class TeamController
@Autowired
constructor(private val teamService: TeamService) {

    @RequestMapping(value = "/teams", method = arrayOf(RequestMethod.GET))
    fun getAllTeams() = teamService.findAll()

    @RequestMapping(value = "/teams", method = arrayOf(RequestMethod.POST))
    @Throws(ServletException::class)
    fun create(@RequestBody json: String): Int {
        Team.fromJson(json)?.let {
            return teamService.create(it)
        }

        throw ServletException("Das Team konnte nicht erstellt werden");
    }
}
