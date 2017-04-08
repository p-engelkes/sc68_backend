package com.pengelkes.controller

import com.pengelkes.service.Team
import com.pengelkes.service.TeamService
import com.pengelkes.service.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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

    @RequestMapping(value = "/teams/{id}", method = arrayOf(RequestMethod.GET))
    fun findById(@PathVariable id: Int): Team? {
        return teamService.findById(id);
    }

    @RequestMapping(value = "/teams", method = arrayOf(RequestMethod.POST))
    @Throws(ServletException::class)
    fun add(@RequestBody json: String): Team {
        Team.fromJson(json)?.let {
            return teamService.create(it)
        }

        throw ServletException("Das Team konnte nicht erstellt werden");
    }

    @RequestMapping(value = "/teams/{id}", method = arrayOf(RequestMethod.POST))
    fun update(@RequestBody json: String, @PathVariable id: Int): Team {
        Team.fromJson(json)?.let {
            return teamService.update(it);
        }

        throw ServletException("Das Team konnte nicht erstellt werden");
    }

    @RequestMapping(value = "/teams/{id}/players", method = arrayOf(RequestMethod.GET))
    fun getAllPlayers(@PathVariable id: Int): List<User> {
        return teamService.findAllPlayersByTeam(id)
    }
}
