package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 18.02.2017.
 */
class GameTest : SpringTestCase() {

    @Autowired
    lateinit var gameService: GameService

    @Test
    fun testGetByTeamAndType() {
        gameService.findByTeamAndType(1, GameType.PREVIOUS).size.should.equal(2)
    }

    @Test
    fun testUpdateGamesByTeamAndTypeWithNoNewValues() {
        databaseTeam.teamId = null
        gameService.updateGamesByTeamAndType(databaseTeam, GameType.PREVIOUS)
        gameService.findByTeamAndType(1, GameType.PREVIOUS).size.should.equal(0)
    }

    @Test
    fun testUpdateGamesByTeamAndTypeWithNewValues() {
        gameService.updateGamesByTeamAndType(databaseTeam, GameType.PREVIOUS).size.should.equal(10)
    }
}