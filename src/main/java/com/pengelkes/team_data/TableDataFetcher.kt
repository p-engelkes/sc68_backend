package com.pengelkes.team_data

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableDataFetcher(val tableData: String) {

    companion object {
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
    }

    fun getAllTableTeams(): List<TableTeam> {
        val allTeamsList = mutableListOf<TableTeam>()
        val teamDataList = getAllTeamData()

        teamDataList.forEach {
            val tableTeam = TableTeam()
            tableTeam.name = getTeamName(it)
            tableTeam.icon = getIcon(it)
            tableTeam.games = getTeamValueBetweenTags(TableTeam.gamesIndex, tableDataStartTag, it).toInt()
            tableTeam.wonGames = getTeamValueBetweenTags(TableTeam.wonGamesIndex, tableDataStartTag, it).toInt()
            tableTeam.tiedGames = getTeamValueBetweenTags(TableTeam.tiedGamesIndex, tableDataStartTag, it).toInt()
            tableTeam.lostGames = getTeamValueBetweenTags(TableTeam.lostGamesIndex, tableDataStartTag, it).toInt()
            tableTeam.goalRatio = getTeamValueBetweenTags(TableTeam.goalRatioIndex, tableDataStartTag, it)
            tableTeam.goalDifference = getTeamValueBetweenTags(
                    TableTeam.goalDifferenceIndex, tableDataStartTag, it).toInt()
            tableTeam.points = getTeamValueBetweenTags(TableTeam.pointsIndex, tableDataStartTag, it).toInt()

            allTeamsList.add(tableTeam)
        }

        return allTeamsList
    }

    fun getAllTeamData(): List<String> {
        val teamDataList = mutableListOf<String>()
        var sanitizedTableData = getDataBetweenTags(tableData, tableBodyStartTag, tableBodyEndTag)
        while (sanitizedTableData.contains(tableRowEndTag)) {
            val teamData = getDataWithTags(sanitizedTableData, tableRowStartTag, tableRowEndTag)
            sanitizedTableData = sanitizedTableData.replace(teamData, "")
            teamDataList.add(teamData)
        }

        return teamDataList
    }

    fun getTeamName(teamDiv: String): String {
        val startingDivIndex = teamDiv.indexOf(clubNameStartingDiv)
        return teamDiv.substring(startingDivIndex + clubNameStartingDiv.length,
                teamDiv.indexOf(closingDiv, startingDivIndex))
    }

    fun getIcon(iconDiv: String): String {
        val startingIconDivIndex = iconDiv.indexOf(logoStartString)
        return iconDiv.substring(startingIconDivIndex + logoStartString.length,
                iconDiv.indexOf(logoEndString, startingIconDivIndex))
    }

    fun getDataWithTags(data: String, startTag: String, endTag: String): String {
        return data.substring(data.indexOf(startTag), data.indexOf(endTag) + endTag.length)
    }

    fun getDataBetweenTags(data: String, startTag: String, endTag: String, startIndex: Int? = null): String {
        var dataString = data
        startIndex?.let {
            dataString = dataString.substring(startIndex, dataString.length)
        }

        return dataString.substring(dataString.indexOf(startTag) + startTag.length, dataString.indexOf(endTag)).trim()
    }

    fun getValueFromTableDataString(tableDataString: String): String {
        return tableDataString.substring(tableDataString.indexOf(">") + 1, tableDataString.length)
    }

    fun findNthIndexOf(n: Int, stringToSearchFor: String, stringToSearchIn: String): Int {
        var index = 0
        for (i in 0..n) {
            index = stringToSearchIn.indexOf(stringToSearchFor, index) + stringToSearchFor.length
        }

        return index - stringToSearchFor.length
    }

    fun getTeamValueBetweenTags(numberOfIndex: Int, stringToSearchFor: String, stringToSearchIn: String): String {
        val valueIndex = findNthIndexOf(numberOfIndex, stringToSearchFor, stringToSearchIn)
        val valueString = getDataBetweenTags(stringToSearchIn, tableDataStartTag, tableDataEndTag, valueIndex)
        return getValueFromTableDataString(valueString)
    }
}

data class TableTeam(var name: String? = null, var icon: String? = null,
                     var games: Int? = null, var wonGames: Int? = null,
                     var tiedGames: Int? = null, var lostGames: Int? = null,
                     var goalRatio: String? = null, var goalDifference: Int? = null,
                     var points: Int? = null) {

    companion object {
        val gamesIndex = 3
        val wonGamesIndex = 4
        val tiedGamesIndex = 5
        val lostGamesIndex = 6
        val goalRatioIndex = 7
        val goalDifferenceIndex = 8
        val pointsIndex = 9
    }
}