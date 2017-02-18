package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.Game.GAME
import com.pengelkes.backend.jooq.tables.records.GameRecord
import com.pengelkes.team_data.GameDataFetcher
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by patrickengelkes on 18/02/2017.
 */
enum class GameType {
    PREVIOUS,
    PAST
}

data class Score(val homeTeamGoals: Int, val awayTeamGoals: Int)

class Game {
    var id: Int? = null
    var gameTime: String? = null
    var homeTeamName: String? = null
    var awayTeamName: String? = null
    var score: Score? = null
    var gameType: GameType? = null

    constructor()

    constructor(id: Int? = null, gameTime: String? = null, homeTeamName: String? = null, awayTeamName: String? = null,
                score: Score? = null, gameType: GameType? = null) {
        this.id = id
        this.gameTime = gameTime
        this.homeTeamName = homeTeamName
        this.awayTeamName = awayTeamName
        this.score = score
        this.gameType = gameType
    }

    constructor(gameRecord: GameRecord) {
        this.id = gameRecord.id
        this.gameTime = gameRecord.gameTime
        this.homeTeamName = gameRecord.homeTeamName
        this.awayTeamName = gameRecord.awayTeamName
        val homeTeamGoals = gameRecord.homeTeamGoals
        val awayTeamGoals = gameRecord.awayTeamGoals
        this.score = Score(homeTeamGoals, awayTeamGoals)
        this.gameType = GameType.valueOf(gameRecord.gameType)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as com.pengelkes.service.Game

        if (id != other.id) return false
        if (gameTime != other.gameTime) return false
        if (homeTeamName != other.homeTeamName) return false
        if (awayTeamName != other.awayTeamName) return false
        if (score != other.score) return false
        if (gameType != other.gameType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (gameTime?.hashCode() ?: 0)
        result = 31 * result + (homeTeamName?.hashCode() ?: 0)
        result = 31 * result + (awayTeamName?.hashCode() ?: 0)
        result = 31 * result + (score?.hashCode() ?: 0)
        result = 31 * result + (gameType?.hashCode() ?: 0)
        return result
    }
}

@Service
interface GameService {
    fun updateGamesByTeamAndType(team: Team, gameType: GameType)
}

@Service
@Transactional
open class GameServiceImpl {

}

@Component
open class GameServiceController @Autowired constructor(private val dsl: DSLContext) {
    fun updateGamesByTeamAndType(team: Team, gameType: GameType) {
        deleteGamesByTeamAndType(team, gameType)
    }

    private fun deleteGamesByTeamAndType(team: Team, gameType: GameType) {
        dsl.deleteFrom(GAME)
                .where(GAME.TEAM_ID.eq(team.id), GAME.GAME_TYPE.eq(gameType.toString()))
    }

    private fun createGamesByTeamAndType(team: Team, gameType: GameType) {
        val games = GameDataFetcher(team, gameType).getAllGames()

        games.forEach {
            val gameRecord = GameRecord()
            gameRecord.id = it.id
        }
    }
}
