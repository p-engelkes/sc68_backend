package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.Game.GAME
import com.pengelkes.backend.jooq.tables.records.GameRecord
import com.pengelkes.team_data.GameDataFetcher
import org.jooq.DSLContext
import org.jooq.Result
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

class Score {
    var homeTeamGoals: Int? = null
    var awayTeamGoals: Int? = null

    constructor()

    constructor(homeTeamGoals: Int, awayTeamGoals: Int) {
        this.homeTeamGoals = homeTeamGoals
        this.awayTeamGoals = awayTeamGoals
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Score

        if (homeTeamGoals != other.homeTeamGoals) return false
        if (awayTeamGoals != other.awayTeamGoals) return false

        return true
    }

    override fun hashCode(): Int {
        var result = homeTeamGoals ?: 0
        result = 31 * result + (awayTeamGoals ?: 0)
        return result
    }


}

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
    fun findByTeamAndType(teamId: Int, gameType: GameType): List<Game>
}

@Service
@Transactional
open class GameServiceImpl
@Autowired
constructor(private val gameServiceController: GameServiceController) : GameService {
    override fun findByTeamAndType(teamId: Int, gameType: GameType): List<Game> {
        return gameServiceController.findByTeamAndType(teamId, gameType)
    }

    override fun updateGamesByTeamAndType(team: Team, gameType: GameType) {
        return gameServiceController.updateGamesByTeamAndType(team, gameType)
    }

}

@Component
open class GameServiceController @Autowired constructor(private val dsl: DSLContext) {
    fun updateGamesByTeamAndType(team: Team, gameType: GameType) {
        deleteGamesByTeamAndType(team, gameType)
        return createGamesByTeamAndType(team, gameType)
    }

    fun findByTeamAndType(teamId: Int, gameType: GameType): List<Game> {
        return getEntities(
                dsl.selectFrom(GAME)
                        .where(GAME.TEAM_ID.eq(teamId))
                        .and(GAME.GAME_TYPE.eq(gameType.toString()))
                        .orderBy(GAME.ID.asc())
                        .fetch())
    }

    private fun getEntities(result: Result<GameRecord>): List<Game> {
        val allGames = mutableListOf<Game>()
        result.forEach { getEntity(it)?.let { allGames.add(it) } }

        return allGames
    }

    private fun getEntity(gameRecord: GameRecord?): Game? {
        gameRecord?.let { return Game(it) }
        return null
    }

    private fun deleteGamesByTeamAndType(team: Team, gameType: GameType) {
        dsl.deleteFrom(GAME)
                .where(GAME.TEAM_ID.eq(team.id), GAME.GAME_TYPE.eq(gameType.toString()))
                .execute()
    }

    private fun createGamesByTeamAndType(team: Team, gameType: GameType) {
        val gameDataFetcher = GameDataFetcher(team, gameType)
        val games = gameDataFetcher.getAllGames()

        val gameRecords = createGameRecords(games, team)
        gameRecords.forEach {
            dsl.newRecord(GAME, it).store()
        }
    }

    private fun createGameRecords(games: List<Game>, team: Team): List<GameRecord> {
        val gameRecords = mutableListOf<GameRecord>()

        games.forEach {
            val gameRecord = GameRecord()
            gameRecord.gameTime = it.gameTime
            gameRecord.homeTeamName = it.homeTeamName
            gameRecord.awayTeamName = it.awayTeamName
            gameRecord.gameType = it.gameType.toString()
            gameRecord.teamId = team.id
            it.score?.let {
                gameRecord.homeTeamGoals = it.homeTeamGoals
                gameRecord.awayTeamGoals = it.awayTeamGoals
            }

            gameRecords.add(gameRecord)
        }

        return gameRecords
    }
}
