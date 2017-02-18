package com.pengelkes.team_data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableFetcherTest {

    lateinit var tableFetcher: TableFetcher
    lateinit var tableDataString: String

    @Before
    fun setup() {
        tableDataString = javaClass.classLoader.getResource("files/table_data/table_data.txt")
                .readText(Charsets.UTF_8)

        tableFetcher = TableFetcher(tableDataString)
    }

    @Test
    fun getTable() {
        val expected = hashMapOf<Int, TableTeam>()
        expected[1] = TableTeam(name = "SpVg Emsdetten 05 II",
                icon = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC00000JVV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130",
                games = 15, wonGames = 12, tiedGames = 2, lostGames = 1, goalRatio = "52 : 13", goalDifference = 39,
                points = 38
        )
        expected[14] = TableTeam(name = "Portu Rheine",
                icon = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC000012VV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130",
                games = 14, wonGames = 0, tiedGames = 1, lostGames = 13, goalRatio = "7 : 79", goalDifference = -72,
                points = 1
        )
        val table = tableFetcher.getTable()

        assertEquals(14, table.size)
        assertEquals(expected[1], table[1])
        assertEquals(expected[14], table[14])
    }


}