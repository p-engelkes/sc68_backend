package com.pengelkes.team_data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableDataFetcherTest {

    lateinit var tableDataFetcher: TableDataFetcher
    lateinit var tableDataString: String

    @Before
    fun setup() {
        tableDataString = javaClass.classLoader.getResource("files/table_data/table_data.txt")
                .readText(Charsets.UTF_8)

        tableDataFetcher = TableDataFetcher(tableDataString)
    }

    @Test
    fun getAllTableTeams() {
        val expectedNumberOfTeams = 14

        assertEquals(expectedNumberOfTeams, tableDataFetcher.getAllTableTeams().size)
    }

    @Test
    fun getAllTeamData() {
        val expectedNumberOfDataBlocks = 14

        assertEquals(expectedNumberOfDataBlocks, tableDataFetcher.getAllTeamData().size)
    }

    @Test
    fun getTeamName() {
        val expectedTeamName = "SpVg Emsdetten 05 II"
        val teamDiv = javaClass.classLoader.getResource("files/table_data/data_between_tags.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expectedTeamName, tableDataFetcher.getTeamName(teamDiv))
    }

    @Test
    fun getIcon() {
        val expectedLogoUrl = "www.fussball.de/export.media/-/action/getLogo/format/3/id/00ES8GN8TC00000JVV0AG08LVUPGND5I/verband/0123456789ABCDEF0123456700004130"
        val iconDiv = javaClass.classLoader.getResource("files/table_data/data_between_tags.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expectedLogoUrl, tableDataFetcher.getIcon(iconDiv))
    }

    @Test
    fun getDataBetweenTags() {
        val expected = javaClass.classLoader.getResource("files/table_data/sanitized_table_data.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, tableDataFetcher.getDataBetweenTags(
                tableDataString, TableDataFetcher.tableBodyStartTag, TableDataFetcher.tableBodyEndTag
        ))
    }

    @Test
    fun getDataBetweenTagsStartingAtSpecificIndex() {
        val expected = ">15"
        val data = javaClass.classLoader.getResource("files/table_data/data_between_tags.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, tableDataFetcher.getDataBetweenTags(
                data, TableDataFetcher.tableDataStartTag, TableDataFetcher.tableDataEndTag, 638
        ))
    }

    @Test
    fun getDataWithTags() {
        val data = javaClass.classLoader.getResource("files/table_data/sanitized_table_data.txt")
                .readText(Charsets.UTF_8)
        val expected = javaClass.classLoader.getResource("files/table_data/data_between_tags.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, tableDataFetcher.getDataWithTags(
                data, TableDataFetcher.tableRowStartTag, TableDataFetcher.tableRowEndTag
        ))
    }

    @Test
    fun getValueFromTableDataString() {
        val expected = "15"

        assertEquals(expected, tableDataFetcher.getValueFromTableDataString(">15"))
    }

    @Test
    fun findNthIndexOf() {
        val stringToSearchIn = javaClass.classLoader.getResource("files/table_data/sanitized_table_data.txt")
                .readText(Charsets.UTF_8)

        val expectedIndex = 654
        assertEquals(expectedIndex,
                tableDataFetcher.findNthIndexOf(4, stringToSearchFor = "<td", stringToSearchIn = stringToSearchIn))
    }

    @Test
    fun getTeamValueBetweenTags() {
        val expected = "15"
        val stringToSearchIn = javaClass.classLoader.getResource("files/table_data/data_between_tags.txt")
                .readText(Charsets.UTF_8)

        assertEquals(expected, tableDataFetcher.getTeamValueBetweenTags(3,
                TableDataFetcher.tableDataStartTag, stringToSearchIn))
    }
}