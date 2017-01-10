package com.pengelkes.controller

import com.pengelkes.DatabaseTestCase
import com.pengelkes.service.Team
import com.pengelkes.service.TeamService
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by patrickengelkes on 10/01/2017.
 */
class TeamTest : DatabaseTestCase() {

    @Autowired
    lateinit var teamService: TeamService

    companion object {
        val teamName = "Herren 1. Mannschaft"
        val trainingTimes = hashMapOf<String, String>(Pair("Wednesday", "19:00"), Pair("Friday", "19:00"))
        val team = Team(teamName, trainingTimes)
    }

    @Test
    fun testFindAll() {
        teamService.findAll().size.should.equal(0)
    }

    @Test
    fun testFindByName() {
        teamService.create(team)
        val team = teamService.findByName(teamName)
        team.should.not.be.`null`
        if (team != null) {
            team.name.should.equal(teamName)
            team.trainingTimes.should.equal(trainingTimes)
        }
    }
}