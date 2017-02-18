package com.pengelkes.team_data

/**
 * Created by pengelkes on 11.02.2017.
 */
class TableFetcher(private val tableData: String) {

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

    fun getTable(): Map<Int, TableTeam> {
        val table = mutableMapOf<Int, TableTeam>()

        getTableData().forEachIndexed { position, teamData ->
            val tableTeam = TableTeam()
            tableTeam.name = teamData.getTeamName()
            tableTeam.icon = teamData.getTeamIcon()
            tableTeam.games = teamData.nthValueBetweenTag(TableTeam.gamesIndex, tableDataStartTag).toInt()
            tableTeam.wonGames = teamData.nthValueBetweenTag(TableTeam.wonGamesIndex, tableDataStartTag).toInt()
            tableTeam.tiedGames = teamData.nthValueBetweenTag(TableTeam.tiedGamesIndex, tableDataStartTag).toInt()
            tableTeam.lostGames = teamData.nthValueBetweenTag(TableTeam.lostGamesIndex, tableDataStartTag).toInt()
            tableTeam.goalRatio = teamData.nthValueBetweenTag(TableTeam.goalRatioIndex, tableDataStartTag)
            tableTeam.goalDifference = teamData.nthValueBetweenTag(
                    TableTeam.goalDifferenceIndex, tableDataStartTag).toInt()
            tableTeam.points = teamData.nthValueBetweenTag(TableTeam.pointsIndex, tableDataStartTag).toInt()

            table.put(position + 1, tableTeam)
        }

        return table
    }

    private fun getTableData(): List<String> {
        val teamDataList = mutableListOf<String>()
        var sanitizedTableData = tableData.valueBetweenTag(tableBodyStartTag, tableBodyEndTag)
        while (sanitizedTableData.contains(tableRowEndTag)) {
            val teamData = sanitizedTableData.getDataWithTags(tableRowStartTag, tableRowEndTag)
            sanitizedTableData = sanitizedTableData.replace(teamData, "")
            teamDataList.add(teamData)
        }

        return teamDataList
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