package com.pengelkes.controller

import com.pengelkes.service.Game
import com.pengelkes.service.GameService
import com.pengelkes.service.GameType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by pengelkes on 20.02.2017.
 */
@RestController
@RequestMapping("/api")
class GameController @Autowired constructor(val gameService: GameService) {
    @RequestMapping(value = "/games", method = arrayOf(RequestMethod.GET))
    fun findByTeamAndGameType(@RequestParam("teamId", required = true) teamId: Int,
                              @RequestParam("gameType", required = true) gameTypeString: String): List<Game> {
        return gameService.findByTeamAndType(teamId, GameType.valueOf(gameTypeString))
    }
}