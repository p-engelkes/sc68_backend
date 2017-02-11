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
    }

    fun getAllTableTeams(): List<TableTeam> {
        val allTeamsList = mutableListOf<TableTeam>()
        val allTeamNames = getAllTeamNames().toList()
        val allTeamIcons = getAllTeamIcons().toList()
        (0..allTeamNames.count() - 1).mapTo(allTeamsList)
        { TableTeam(getTeamName(allTeamNames[it].value), allTeamIcons[it].value) }

        return allTeamsList
    }

    fun getAllTeamNames(): Sequence<MatchResult> {
        val teamRegex = Regex("<div class=\"club-name\">.*</div>")
        return teamRegex.findAll(tableData)
    }

    fun getTeamName(teamDiv: String): String {
        return teamDiv.substring(clubNameStartingDiv.length, teamDiv.indexOf(closingDiv))
    }

    fun getAllTeamIcons(): Sequence<MatchResult> {
        val iconRegex = Regex("<div class=\"club-logo.*</div>")
        return iconRegex.findAll(tableData)
    }

    fun getIcon(iconDiv: String): String {
        return iconDiv.substring(iconDiv.indexOf(logoStartString) + logoStartString.length,
                iconDiv.indexOf(logoEndString))
    }
}

data class TableTeam(val name: String, val icon: String)