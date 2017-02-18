package com.pengelkes.team_data

import java.net.URL

data class Game(var gameTime: String? = null, var homeTeamName: String? = null,
                var awayTeamName: String? = null, var score: Score? = null)

data class Score(val homeTeamGoals: Int, val awayTeamGoals: Int)

class GameDataFetcher(var completeGameInfo: String, var calculateScore: Boolean) {

    companion object {
        val tableBodyStart = "<tbody>"
        val tableBodyEnd = "</tbody>"
        val tableRowEnd = "</tr>"
        val goalsDiv = "<div class=\"goals\">"
    }

    fun getAllGames(): List<Game> {
        val allGames = mutableListOf<Game>()
        var gameData = getGameData()
        while (gameData.contains(tableRowEnd)) {
            val game = Game()
            val gameString = gameData.getSingleGameData()
            gameData = gameData.replace(gameString, "")
            game.gameTime = gameString.getGameTime()
            val gameWithoutTimeData = gameString.removeGameTime()

            game.homeTeamName = gameWithoutTimeData.getClub()

            val gameWithoutTimeAndFirstClub = gameWithoutTimeData.removeFirstClub(game.homeTeamName!!)
            game.awayTeamName = gameWithoutTimeAndFirstClub.getClub()

            val score = gameWithoutTimeAndFirstClub.getScoreString()

            if (calculateScore) {
                if (score.contains("colon")) {
                    //game was not cancelled
                    val gameDetailsUrl = gameWithoutTimeAndFirstClub.getGameDetailsUrl()
                    game.score = URL(gameDetailsUrl).readText(Charsets.UTF_8).getScore()
                }
            }

            allGames.add(game)
        }

        return allGames
    }

    private fun getGameData(): String {
        val gameDataStart = completeGameInfo.indexOf(tableBodyStart)
        val gameDataEnd = completeGameInfo.indexOf(tableBodyEnd)

        return completeGameInfo.substring(gameDataStart + tableBodyStart.length, gameDataEnd).trim()
    }
}

private fun String.getSingleGameData(): String {
    var matchEndIndex = 0
    for (i in 0..2) {
        matchEndIndex = this.indexOf(GameDataFetcher.tableRowEnd, startIndex = matchEndIndex + GameDataFetcher.tableRowEnd.length)
    }

    return this.substring(0, matchEndIndex + GameDataFetcher.tableRowEnd.length)
}

private fun String.getGameTime(): String {
    val gameTimeLineStart = this.indexOf("<td")
    val gameTimeLineEnd = this.indexOf("</tr>")
    val gameTimeLine = this.substring(gameTimeLineStart, gameTimeLineEnd)

    val gameTimeStart = gameTimeLine.indexOf(">")
    val gameTimeEnd = gameTimeLine.indexOf(" | ")

    return gameTimeLine.substring(gameTimeStart + 1, gameTimeEnd)
}

private fun String.removeGameTime(): String {
    var timeEndIndex = 0
    for (i in 0..1) {
        timeEndIndex = this.indexOf(GameDataFetcher.tableRowEnd, startIndex = timeEndIndex + GameDataFetcher.tableRowEnd.length)
    }

    return this.substring(timeEndIndex + GameDataFetcher.tableRowEnd.length, this.length).trim()
}

private fun String.getClub(): String {
    val clubDiv = "<div class=\"club-name\">"
    val clubData = this.substring(this.indexOf(clubDiv) + clubDiv.length, this.length)
    return clubData.substring(0, clubData.indexOf("</div>"))
}

private fun String.removeFirstClub(firstClub: String): String {
    return this.substring(this.indexOf(firstClub) + firstClub.length, this.length)
}

private fun String.getScoreString(): String {
    val columnScoreDiv = "<td class=\"column-score\">"
    return this.substring(this.indexOf(columnScoreDiv) + columnScoreDiv.length, this.length).trim()
}

private fun String.getGameDetailsUrl(): String {
    val columnDetailDiv = "<td class=\"column-detail\">"
    val gameDetails = this.substring(this.indexOf(columnDetailDiv) + columnDetailDiv.length, this.length)
    val anchor = "<a href=\""
    return gameDetails.substring(gameDetails.indexOf(anchor) + anchor.length, gameDetails.indexOf("\">"))
}

private fun String.getScore(): Score {
    val homeTeamString = this.getHomeTeamGoals()
    val awayTeamString = this.getAwayTeamGoals()

    return Score(homeTeamString.countGoals(), awayTeamString.countGoals())
}

private fun String.getHomeTeamGoals(): String {
    val homeTeamGoals = this.substring(
            this.indexOf(GameDataFetcher.goalsDiv) + GameDataFetcher.goalsDiv.length, this.length)
    return homeTeamGoals.substring(0, homeTeamGoals.indexOf("</div>"))
}

private fun String.getAwayTeamGoals(): String {
    val awayTeamGoals = this.substring(
            this.indexOf(GameDataFetcher.goalsDiv, this.indexOf(GameDataFetcher.goalsDiv) + GameDataFetcher.goalsDiv.length) + GameDataFetcher.goalsDiv.length, this.length)
    return awayTeamGoals.substring(0, awayTeamGoals.indexOf("</div")).trim()
}

private fun String.countGoals(): Int {
    val goalsRegex = Regex("\\d{1,2}'")
    return goalsRegex.findAll(this).count()
}