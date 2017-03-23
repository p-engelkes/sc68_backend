package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by patrickengelkes on 10/01/2017.
 */
class TeamTest : SpringTestCase() {

    @Autowired
    lateinit var teamService: TeamService

    companion object {
        val testName = "Herren 1. Mannschaft"
        val teamName = "Herren 3. Mannschaft"
        val team = Team(name = teamName, trainingTimes = hashMapOf(Pair("Friday", "19:00")))
    }

    @Test
    fun testCreate() {
        val size = teamService.findAll().size
        teamService.create(team)
        teamService.findAll().size.should.equal(size + 1)
    }

    @Test
    @Ignore
    fun testFindAll() {
        teamService.findAll().size.should.equal(databaseTeams.size)
    }

    @Test
    fun testFindByName() {
        val team = teamService.findByName(testName)
        team.should.not.be.`null`
        if (team != null) {
            team.name.should.equal(testName)
        }
    }

    @Test
    fun testFindAllPlayersByTeam() {
        val expected = listOf(databaseUser)

        teamService.findAllPlayersByTeam(1).should.equal(expected)
    }

    @Test
    @Ignore
    fun testFindByOldClassId() {
        var expected = listOf(databaseTeamOne, databaseTeamTwo)
        teamService.findByOldClass(1).should.equal(expected)

        expected = listOf(databaseTeamThree)
        teamService.findByOldClass(2).should.equal(expected)
    }

    @Test
    @Ignore
    fun testFindByOldClassIdWithArticle() {
        var expected = listOf(databaseTeamOne, databaseTeamTwo)
        teamService.findByOldClassWithArticle(1).should.equal(expected)

        expected = emptyList()
        teamService.findByOldClassWithArticle(2).should.equal(expected)
    }
}