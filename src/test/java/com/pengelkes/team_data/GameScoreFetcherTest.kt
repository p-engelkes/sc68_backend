package com.pengelkes.team_data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


/**
 * Created by pengelkes on 10.02.2017.
 */
class GameScoreFetcherTest {

    lateinit var gameDataFetcher: GameDataFetcher
    lateinit var gameScoreData: String

    @Before
    fun setup() {
        gameScoreData = javaClass.classLoader.getResource("files/game_data/4_to_4_game.txt")
                .readText(Charsets.UTF_8)
        this.gameDataFetcher = GameDataFetcher(gameScoreData, true)
    }

    @Test
    fun getHomeTeamGoals() {
        val expected = "(27') <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xE875;&#xEA4D;&#xE67F;&#xEA5D;&#xE974;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xE923;&#xEA4D;&#xEA4D;&#xE82B;&#xE68B;&#xE876;&#xE974;&#xE974;</span> | (29', 90' +4) <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xEAAF;&#xEA97;&#xEA4D;&#xEAFD;&#xE876;&#xE974;&#xE8B6;&#xEA4D;&#xE82B;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xEAAD;&#xE7B2;&#xE719;&#xE974;</span> | (65') <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xEAAD;&#xEB27;&#xE6D4;&#xE876;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xE875;&#xEA4D;&#xE82B;&#xE749;&#xE8E7;&#xEA5D;&#xE974;&#xEAA0;</span>"

        assertEquals(expected, gameDataFetcher.getHomeTeamGoals(gameScoreData))
    }

    @Test
    fun getAwayTeamGoals() {
        val expected = javaClass.classLoader.getResource("files/game_data/4_to_4_away_team_goals.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, gameDataFetcher.getAwayTeamGoals(gameScoreData))
    }

    @Test
    fun countGoals() {
        val scoreString = "(27') <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xE875;&#xEA4D;&#xE67F;&#xEA5D;&#xE974;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xE923;&#xEA4D;&#xEA4D;&#xE82B;&#xE68B;&#xE876;&#xE974;&#xE974;</span> | (29', 90' +4) <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xEAAF;&#xEA97;&#xEA4D;&#xEAFD;&#xE876;&#xE974;&#xE8B6;&#xEA4D;&#xE82B;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xEAAD;&#xE7B2;&#xE719;&#xE974;</span> | (65') <span data-obfuscation=\"z8gmnuq0\" class=\"hidden-small inline\">&#xEAAD;&#xEB27;&#xE6D4;&#xE876;</span><span data-obfuscation=\"z8gmnuq0\">&#x0020;&#xE875;&#xEA4D;&#xE82B;&#xE749;&#xE8E7;&#xEA5D;&#xE974;&#xEAA0;</span>"
        val expected = 4

        assertEquals(expected, gameDataFetcher.countGoals(scoreString))
    }
}