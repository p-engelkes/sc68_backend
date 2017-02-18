package com.pengelkes.team_data

import com.pengelkes.service.Game
import com.pengelkes.service.GameType
import com.pengelkes.service.Score
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


/**
 * Created by pengelkes on 10.02.2017.
 */
class GameDataFetcherTest {
    lateinit var gameData: String
    lateinit var gameDataFetcher: GameDataFetcher

    @Before
    fun setup() {
        val gameDataFile = javaClass.classLoader.getResource("files/previous_games_data/example_match_data.txt")
        gameData = gameDataFile.readText(Charsets.UTF_8)
        gameDataFetcher = GameDataFetcher(null, GameType.PREVIOUS)
    }

    @Test
    fun getAllGamesWithGameInfoString() {
        gameDataFetcher.completeGameInfo = gameData
        val firstGame = Game(
                gameTime = "Samstag, 28.01.2017 - 17:00 Uhr",
                homeTeamName = "SuS Neuenkirchen III",
                awayTeamName = "SKICLUB NORDWEST RHEINE"
        )
        val lastGame = Game(
                gameTime = "Sonntag, 09.10.2016 - 15:00 Uhr",
                homeTeamName = "Skiclub Nordwest Rheine",
                awayTeamName = "FC Eintracht Rheine III",
                score = Score(4, 3)
        )
        val expectedNumberOfGames = 10
        val games = gameDataFetcher.getAllGames()

        assertEquals(expectedNumberOfGames, gameDataFetcher.getAllGames().size)
        assertEquals(firstGame, games[0])
        assertEquals(lastGame, games[9])
    }

    @Test
    fun getAllGamesWithoutAnyGameInfo() {
        assertEquals(emptyList<Game>(), gameDataFetcher.getAllGames())
    }
}