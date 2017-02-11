package com.pengelkes.team_data

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
        gameDataFetcher = GameDataFetcher(gameData)
    }

    @Test
    fun getMatchData() {
        val expected = javaClass.classLoader.getResource("files/previous_games_data/expected_previous_game_data.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.getGameData())
    }

    @Test
    fun getSingleMatchFromMatchData() {
        val matchData = javaClass.classLoader.getResource("files/previous_games_data/expected_previous_game_data.txt")
                .readText(Charsets.UTF_8)
        val expected = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.getSingleGameData(matchData))
    }

    @Test
    fun getGameTime() {
        val gameData = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data.txt")
                .readText(Charsets.UTF_8)
        val expected = "Samstag, 28.01.2017 - 17:00 Uhr"

        assertEquals(expected, gameDataFetcher.getGameTime(gameData))
    }

    @Test
    fun removeGameTime() {
        val gameData = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data.txt")
                .readText(Charsets.UTF_8)
        val expected = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.removeGameTime(gameData))
    }

    @Test
    fun getClubFromString() {
        val gameDataWithoutTime = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time.txt")
                .readText(Charsets.UTF_8)
        val expected = "SuS Neuenkirchen III"

        assertEquals(expected, gameDataFetcher.getClubFromString(gameDataWithoutTime))
    }

    @Test
    fun removeFirstClub() {
        val gameDataWithoutTime = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time.txt")
                .readText(Charsets.UTF_8)
        val expected = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time_and_club.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.removeFirstClub(gameDataWithoutTime, "SuS Neuenkirchen III"))
    }

    @Test
    fun getScoreString() {
        val gameDataWithoutTimeAndClub = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time_and_club.txt")
                .readText(Charsets.UTF_8)
        val expected = javaClass.classLoader.getResource("files/previous_games_data/score.txt").readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.getScoreString(gameDataWithoutTimeAndClub))
    }

    @Test
    fun getGameDetailsUrl() {
        val gameDataWithoutTimeAndClub = javaClass.classLoader.getResource("files/previous_games_data/expected_game_data_without_time_and_club.txt")
                .readText(Charsets.UTF_8)
        val expected = "http://www.fussball.de/spiel/sus-neuenkirchen-iii-skiclub-nordwest-rheine/-/spiel/01U1FPA2A0000000VS54898EVU34LTP2"

        assertEquals(expected, gameDataFetcher.getGameDetailsUrl(gameDataWithoutTimeAndClub))
    }
}