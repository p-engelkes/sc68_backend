package com.pengelkes.team_data

import java.net.URL

fun main(args: Array<String>) {
    val previousGamesString = URL("http://www.fussball.de/ajax.team.prev.games/-/team-id/011MICFJLG000000VTVG0001VTR8C1K7")
            .readText(Charsets.UTF_8)

    val gameDataFetcher = GameDataFetcher(previousGamesString)
    gameDataFetcher.getAllGames().forEach(::println)
}


data class Game(var gameTime: String? = null, var homeTeamName: String? = null,
                var awayTeamName: String? = null, var score: Score? = null)

data class Score(val homeTeamGoals: Int, val awayTeamGoals: Int)

class GameDataFetcher(val completeGameInfo: String) {

    companion object {
        val tableBodyStart = "<tbody>"
        val tableBodyEnd = "</tbody>"
        val tableRowEnd = "</tr>"
    }

    fun getGameData(): String {
        val gameDataStart = completeGameInfo.indexOf(tableBodyStart)
        val gameDataEnd = completeGameInfo.indexOf(tableBodyEnd)

        return completeGameInfo.substring(gameDataStart + tableBodyStart.length, gameDataEnd).trim()
    }

    fun getAllGames(): List<Game> {
        val allGames = mutableListOf<Game>()
        var gameData = getGameData()
        while (gameData.contains(tableRowEnd)) {
            val game = Game()
            val gameString = getSingleGameData(gameData)
            gameData = gameData.replace(gameString, "")
            game.gameTime = getGameTime(gameString)
            val gameWithoutTimeData = removeGameTime(gameString)

            game.homeTeamName = getClubFromString(gameWithoutTimeData)

            val gameWithoutTimeAndFirstClub = removeFirstClub(gameWithoutTimeData, game.homeTeamName!!)
            game.awayTeamName = getClubFromString(gameWithoutTimeAndFirstClub)

            val score = getScoreString(gameWithoutTimeAndFirstClub)

            if (score.contains("colon")) {
                //game was not cancelled
                val gameDetailsUrl = getGameDetailsUrl(gameWithoutTimeAndFirstClub)
                val gameScoreFetcher = GameScoreFetcher(URL(gameDetailsUrl).readText(Charsets.UTF_8))
                game.score = gameScoreFetcher.getScore()
            }

            allGames.add(game)
        }

        return allGames
    }

    fun getSingleGameData(gameData: String): String {
        var matchEndIndex = 0
        for (i in 0..2) {
            matchEndIndex = gameData.indexOf(tableRowEnd, startIndex = matchEndIndex + tableRowEnd.length)
        }

        return gameData.substring(0, matchEndIndex + tableRowEnd.length)
    }

    fun getGameTime(game: String): String {
        val gameTimeLineStart = game.indexOf("<td")
        val gameTimeLineEnd = game.indexOf("</tr>")
        val gameTimeLine = game.substring(gameTimeLineStart, gameTimeLineEnd)

        val gameTimeStart = gameTimeLine.indexOf(">")
        val gameTimeEnd = gameTimeLine.indexOf(" | ")

        return gameTimeLine.substring(gameTimeStart + 1, gameTimeEnd)
    }

    fun removeGameTime(game: String): String {
        var timeEndIndex = 0
        for (i in 0..1) {
            timeEndIndex = game.indexOf(tableRowEnd, startIndex = timeEndIndex + tableRowEnd.length)
        }

        return game.substring(timeEndIndex + tableRowEnd.length, game.length).trim()
    }

    fun getClubFromString(data: String): String {
        val clubDiv = "<div class=\"club-name\">"
        val clubData = data.substring(data.indexOf(clubDiv) + clubDiv.length, data.length)
        return clubData.substring(0, clubData.indexOf("</div>"))
    }

    fun removeFirstClub(game: String, firstClub: String): String {
        return game.substring(game.indexOf(firstClub) + firstClub.length, game.length)
    }

    fun getScoreString(game: String): String {
        val columnScoreDiv = "<td class=\"column-score\">"
        return game.substring(game.indexOf(columnScoreDiv) + columnScoreDiv.length, game.length).trim()
    }

    fun getGameDetailsUrl(game: String): String {
        val columnDetailDiv = "<td class=\"column-detail\">"
        val gameDetails = game.substring(game.indexOf(columnDetailDiv) + columnDetailDiv.length, game.length)
        val anchor = "<a href=\""
        return gameDetails.substring(gameDetails.indexOf(anchor) + anchor.length, gameDetails.indexOf("\">"))
    }
}

class GameScoreFetcher(val gameScoreData: String) {

    companion object {
        val goalsDiv = "<div class=\"goals\">"
    }

    fun getScore(): Score {
        val homeTeamString = getHomeTeamGoals()
        val awayTeamString = getAwayTeamGoals()

        return Score(countGoals(homeTeamString), countGoals(awayTeamString))
    }

    fun getHomeTeamGoals(): String {
        val homeTeamGoals = gameScoreData.substring(
                gameScoreData.indexOf(goalsDiv) + goalsDiv.length, gameScoreData.length)
        return homeTeamGoals.substring(0, homeTeamGoals.indexOf("</div>"))
    }

    fun getAwayTeamGoals(): String {
        val awayTeamGoals = gameScoreData.substring(
                gameScoreData.indexOf(goalsDiv, gameScoreData.indexOf(goalsDiv) + goalsDiv.length) + goalsDiv.length, gameScoreData.length)
        return awayTeamGoals.substring(0, awayTeamGoals.indexOf("</div")).trim()
    }

    fun countGoals(scoreString: String): Int {
        val goalsRegex = Regex("\\d{1,2}'")
        return goalsRegex.findAll(scoreString).count()
    }
}