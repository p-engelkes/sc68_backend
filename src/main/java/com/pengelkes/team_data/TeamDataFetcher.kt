package com.pengelkes.team_data

import java.net.URL


/**
 * Created by patrickengelkes on 05/02/2017.
 */
fun main(args: Array<String>) {
    val previousMatchData =
            URL("http://www.fussball.de/ajax.team.prev.games/-/team-id/011MICFJLG000000VTVG0001VTR8C1K7")
                    .readText(Charsets.UTF_8)
    val matchDataStart = previousMatchData.indexOf("<tbody>")
    val matchDataEnd = previousMatchData.indexOf("</tbody>")

    var matchData = previousMatchData.substring(matchDataStart + "<tbody>".length, matchDataEnd)

    while (matchData.contains("</tr>")) {
        var matchEndIndex = 0
        var match: String
        for (i in 0..2) {
            matchEndIndex = matchData.indexOf("</tr>", startIndex = matchEndIndex + 5)
        }
        match = matchData.substring(0, matchEndIndex + 5)
        matchData = matchData.replace(match, "")

        val matchTimeLineStart = match.indexOf("<td")
        val matchTimeLineEnd = match.indexOf("</tr>")
        val matchTimeLine = match.substring(matchTimeLineStart, matchTimeLineEnd)

        val matchTimeStart = matchTimeLine.indexOf(">")
        val matchTimeEnd = matchTimeLine.indexOf(" | ")
        val matchTime = matchTimeLine.substring(matchTimeStart + 1, matchTimeEnd)

        match = match.substring(matchTimeLineEnd + "</tr>".length, match.length)
        match = match.substring(match.indexOf("</tr>") + "</tr>".length, match.length)

        val firstClub = getClubFromString(match)

        match = match.substring(match.indexOf(firstClub) + firstClub.length, match.length)

        val secondClub = getClubFromString(match)

        val columnScoreDiv = "<td class=\"column-score\">"
        val score = match.substring(match.indexOf(columnScoreDiv) + columnScoreDiv.length, match.length)

        if (score.contains("colon")) {
            val columnDetailDiv = "<td class=\"column-detail\">"
            match = match.substring(match.indexOf(columnDetailDiv) + columnDetailDiv.length, match.length)

            val anchor = "<a href=\""
            val gameDetails = match.substring(match.indexOf(anchor) + anchor.length, match.indexOf("\">"))

            val gameDetailsData = URL(gameDetails)
                    .readText(Charsets.UTF_8)

            val goalsDiv = "<div class=\"goals\">"
            var homeTeamGoals = gameDetailsData.substring(gameDetailsData.indexOf(goalsDiv) + goalsDiv.length, gameDetailsData.length)
            homeTeamGoals = homeTeamGoals.substring(0, homeTeamGoals.indexOf("</div>"))

            val goalsRegex = Regex("\\(\\d{1,2}'\\)")
            val homeGoals = goalsRegex.findAll(homeTeamGoals)

            var awayTeamGoals = gameDetailsData.substring(gameDetailsData.indexOf(goalsDiv, gameDetailsData.indexOf(goalsDiv) + goalsDiv.length), gameDetailsData.length)
            awayTeamGoals = awayTeamGoals.substring(0, awayTeamGoals.indexOf("</div>"))

            val awayGoals = goalsRegex.findAll(awayTeamGoals)

            println("$firstClub ${homeGoals.count()} : ${awayGoals.count()} $secondClub ")
        }
    }
}

fun getClubFromString(data: String): String {
    val clubDiv = "<div class=\"club-name\">"
    val clubData = data.substring(data.indexOf(clubDiv) + clubDiv.length, data.length)
    return clubData.substring(0, clubData.indexOf("</div>"))
}
