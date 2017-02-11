package com.pengelkes.team_data

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableDataFetcherTest {

    lateinit var tableDataFetcher: TableDataFetcher

    @Before
    fun setup() {
        val tableDataString = javaClass.classLoader.getResource("files/table_data/table_data.txt")
                .readText(Charsets.UTF_8)

        tableDataFetcher = TableDataFetcher(tableDataString)
    }

    @Test
    fun getAllTableTeams() {
        val expectedNumberOfTeams = 14

        assertEquals(expectedNumberOfTeams, tableDataFetcher.getAllTableTeams().size)
    }

    @Test
    fun getAllTeamNames() {
        val expectedNumberOfTeams = 14

        assertEquals(expectedNumberOfTeams, tableDataFetcher.getAllTeamNames().count())
    }

    @Test
    fun getTeamName() {
        val expectedTeamName = "Skiclub Nordwest Rheine"
        val teamDiv = "<div class=\"club-name\">Skiclub Nordwest Rheine</div>"

        assertEquals(expectedTeamName, tableDataFetcher.getTeamName(teamDiv))
    }

    @Test
    fun getAllTeamIcons() {
        val expectedNumberOfIcons = 14

        assertEquals(expectedNumberOfIcons, tableDataFetcher.getAllTeamIcons().count())
    }

    @Test
    fun getIcon() {
        val expectedLogoUrl = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC00001DVV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130"
        val iconDiv = "<div class=\"club-logo table-image\"><span data-responsive-image=\"//www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC00001DVV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130\"></span></div>"

        assertEquals(expectedLogoUrl, tableDataFetcher.getIcon(iconDiv))
    }
}