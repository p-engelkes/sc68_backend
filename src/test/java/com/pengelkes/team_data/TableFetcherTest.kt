package com.pengelkes.team_data

import com.pengelkes.service.TableTeam
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableFetcherTest {

    lateinit var tableFetcher: TableFetcher
    lateinit var tableData: String

    @Before
    fun setup() {
        tableData = javaClass.classLoader.getResource("files/table_data/table_data.txt")
                .readText(Charsets.UTF_8)

        tableFetcher = TableFetcher()
    }

    @Test
    fun getTable() {
        tableFetcher.tableData = tableData
        val expected = mutableListOf<TableTeam>()
        expected.add(TableTeam(name = "SpVg Emsdetten 05 II", position = 1,
                icon = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC00000JVV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130",
                games = 15, wonGames = 12, tiedGames = 2, lostGames = 1, goalRatio = "52 : 13", goalDifference = 39,
                points = 38
        ))
        (0..12).forEach { expected.add(TableTeam()) }
        expected[13] = TableTeam(name = "Portu Rheine", position = 14,
                icon = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC000012VV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130",
                games = 14, wonGames = 0, tiedGames = 1, lostGames = 13, goalRatio = "7 : 79", goalDifference = -72,
                points = 1
        )
        val table = tableFetcher.getTable()

        assertEquals(14, table!!.tableTeams.size)
        assertEquals(expected[0], table.tableTeams[0])
        assertEquals(expected[13], table.tableTeams[13])
    }


}