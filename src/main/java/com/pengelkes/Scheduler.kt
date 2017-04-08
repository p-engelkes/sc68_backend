package com.pengelkes

import com.pengelkes.service.GameService
import com.pengelkes.service.GameType
import com.pengelkes.service.TableService
import com.pengelkes.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by pengelkes on 03.03.2017.
 */
@Component
open class Scheduler @Autowired constructor(private val gameService: GameService,
                                            private val tableService: TableService,
                                            private val teamService: TeamService) {

    @Scheduled(fixedRate = 21600000)
    fun updateGameAndTableData() {
        teamService.findAll().forEach { team ->
            if (team.soccerInfoId != null && !team.soccerInfoId!!.isEmpty()) {
                gameService.updateGamesByTeamAndType(team, GameType.PREVIOUS)
                tableService.updateTable(team)
            }
        }
    }
}