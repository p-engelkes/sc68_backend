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
        val testName = "Herren 1. Mannschaft"
        val teamName = "Herren 2. Mannschaft"
        val team = Team(teamName, hashMapOf(Pair("Friday", "19:00")))
    }

    @Test
    fun testCreate() {
        val size = teamService.findAll().size
        teamService.create(team)
        teamService.findAll().size.should.equal(size + 1)
    }

    @Test
    fun testFindAll() {
        teamService.findAll().size.should.equal(1)
    }

    @Test
    fun testFindByName() {
        val team = teamService.findByName(testName)
        team.should.not.be.`null`
        if (team != null) {
            team.name.should.equal(testName)
        }
    }
}