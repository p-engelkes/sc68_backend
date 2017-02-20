package com.pengelkes.team_data

import com.pengelkes.service.Table
import com.pengelkes.service.TableTeam
import com.pengelkes.service.Team
import java.net.URL

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableFetcher {
    var team: Team? = null
    var tableData: String? = null

    constructor(team: Team? = null) {
        this.team = team
        team?.soccerInfoId?.let {
            val url = tableUrl + it
            tableData = URL(url).readText(Charsets.UTF_8)
        }
    }

    companion object {
        val tableUrl = "http://www.fussball.de/ajax.team.table/-/staffel/01SNJETL9K000002VS54898DVV2J9QR4-G/team-id/"
        val closingDiv = "</div>"
        val clubNameStartingDiv = "<div class=\"club-name\">"
        val logoStartString = "data-responsive-image=\"//"
        val logoEndString = "\"></span>"
        val tableBodyStartTag = "<tbody>"
        val tableBodyEndTag = "</tbody>"
        val tableRowStartTag = "<tr"
        val tableRowEndTag = "</tr>"
        val tableDataStartTag = "<td"
        val tableDataEndTag = "</td>"
        val gamesIndex = 3
        val wonGamesIndex = 4
        val tiedGamesIndex = 5
        val lostGamesIndex = 6
        val goalRatioIndex = 7
        val goalDifferenceIndex = 8
        val pointsIndex = 9
    }

    fun getTable(): Table? {
        tableData?.let {
            val table = mutableListOf<TableTeam>()

            getTableData().forEachIndexed { position, teamData ->
                val tableTeam = TableTeam()
                tableTeam.position = position + 1
                tableTeam.name = teamData.getTeamName()
                tableTeam.icon = teamData.getTeamIcon()
                tableTeam.games = teamData.nthValueBetweenTag(gamesIndex, tableDataStartTag).toInt()
                tableTeam.wonGames = teamData.nthValueBetweenTag(wonGamesIndex, tableDataStartTag).toInt()
                tableTeam.tiedGames = teamData.nthValueBetweenTag(tiedGamesIndex, tableDataStartTag).toInt()
                tableTeam.lostGames = teamData.nthValueBetweenTag(lostGamesIndex, tableDataStartTag).toInt()
                tableTeam.goalRatio = teamData.nthValueBetweenTag(goalRatioIndex, tableDataStartTag)
                tableTeam.goalDifference = teamData.nthValueBetweenTag(
                        goalDifferenceIndex, tableDataStartTag).toInt()
                tableTeam.points = teamData.nthValueBetweenTag(pointsIndex, tableDataStartTag).toInt()

                table.add(tableTeam)
            }

            return Table(table.sortedBy { it.position }, team)
        }

        return null
    }

    private fun getTableData(): List<String> {
        val teamDataList = mutableListOf<String>()
        tableData?.let {
            var sanitizedTableData = it.valueBetweenTag(tableBodyStartTag, tableBodyEndTag)
            while (sanitizedTableData.contains(tableRowEndTag)) {
                val teamData = sanitizedTableData.getDataWithTags(tableRowStartTag, tableRowEndTag)
                sanitizedTableData = sanitizedTableData.replace(teamData, "")
                teamDataList.add(teamData)
            }
        }

        return teamDataList
    }
}

private fun String.removeTagClosingFromData(): String {
    return this.substring(this.indexOf(">") + 1, this.length).trim()
}

private fun String.valueBetweenTag(startTag: String, endTag: String, startIndex: Int? = null): String {
    var dataString = this
    startIndex?.let {
        dataString = dataString.substring(startIndex, dataString.length)
    }

    val substringStartIndex = dataString.indexOf(startTag) + startTag.length
    val substringEndIndex = dataString.indexOf(endTag)
    return dataString.substring(substringStartIndex, substringEndIndex).trim()
}

private fun String.nthValueBetweenTag(numberOfIndex: Int, stringToSearchFor: String): String {
    val valueIndex = this.nthIndexOf(numberOfIndex, stringToSearchFor)
    val dataBetweenTags = this.valueBetweenTag(TableFetcher.tableDataStartTag, TableFetcher.tableDataEndTag, valueIndex)

    return dataBetweenTags.removeTagClosingFromData()
}

private fun String.nthIndexOf(n: Int, stringToSearchFor: String): Int {
    var index = 0
    for (i in 0..n) {
        index = this.indexOf(stringToSearchFor, index) + stringToSearchFor.length
    }

    return index - stringToSearchFor.length
}

private fun String.getDataWithTags(startTag: String, endTag: String): String {
    return this.substring(this.indexOf(startTag), this.indexOf(endTag) + endTag.length)
}

private fun String.getTeamName(): String {
    val startingDivIndex = this.indexOf(TableFetcher.clubNameStartingDiv)
    return this.substring(startingDivIndex + TableFetcher.clubNameStartingDiv.length,
            this.indexOf(TableFetcher.closingDiv, startingDivIndex))
}

private fun String.getTeamIcon(): String {
    val startingIconDivIndex = this.indexOf(TableFetcher.logoStartString)
    return this.substring(startingIconDivIndex + TableFetcher.logoStartString.length,
            this.indexOf(TableFetcher.logoEndString, startingIconDivIndex))
}