package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.Table.TABLE
import com.pengelkes.backend.jooq.tables.records.TableRecord
import com.pengelkes.team_data.TableFetcher
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by pengelkes on 19.02.2017.
 */
class Table {
    var tableTeams = emptyList<TableTeam>()
    var teamId: Int? = null

    constructor()

    constructor(tableTeams: List<TableTeam>, teamId: Int?) {
        this.tableTeams = tableTeams
        this.teamId = teamId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Table

        if (tableTeams != other.tableTeams) return false
        if (teamId != other.teamId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tableTeams.hashCode()
        result = 31 * result + (teamId ?: 0)
        return result
    }
}

class TableTeam {
    var id: Int? = null
    var position: Int? = null
    var name: String? = null
    var icon: String? = null
    var games: Int? = null
    var wonGames: Int? = null
    var tiedGames: Int? = null
    var lostGames: Int? = null
    var goalRatio: String? = null
    var goalDifference: Int? = null
    var points: Int? = null

    constructor()

    constructor(id: Int? = null, position: Int? = null, name: String? = null, icon: String? = null, games: Int? = null,
                wonGames: Int? = null, tiedGames: Int? = null, lostGames: Int? = null, goalRatio: String? = null,
                goalDifference: Int? = null, points: Int? = null) {
        this.id = id
        this.position = position
        this.name = name
        this.icon = icon
        this.games = games
        this.wonGames = wonGames
        this.tiedGames = tiedGames
        this.lostGames = lostGames
        this.goalRatio = goalRatio
        this.goalDifference = goalDifference
        this.points = points
    }

    constructor(tableRecord: TableRecord) {
        this.id = tableRecord.id
        this.position = tableRecord.position
        this.name = tableRecord.name
        this.icon = tableRecord.icon
        this.games = tableRecord.games
        this.wonGames = tableRecord.wonGames
        this.tiedGames = tableRecord.tiedGames
        this.lostGames = tableRecord.lostGames
        this.goalRatio = tableRecord.goalRatio
        this.goalDifference = tableRecord.goalDifference
        this.points = tableRecord.points
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TableTeam

        if (id != other.id) return false
        if (position != other.position) return false
        if (name != other.name) return false
        if (icon != other.icon) return false
        if (games != other.games) return false
        if (wonGames != other.wonGames) return false
        if (tiedGames != other.tiedGames) return false
        if (lostGames != other.lostGames) return false
        if (goalRatio != other.goalRatio) return false
        if (goalDifference != other.goalDifference) return false
        if (points != other.points) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (position ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (games ?: 0)
        result = 31 * result + (wonGames ?: 0)
        result = 31 * result + (tiedGames ?: 0)
        result = 31 * result + (lostGames ?: 0)
        result = 31 * result + (goalRatio?.hashCode() ?: 0)
        result = 31 * result + (goalDifference ?: 0)
        result = 31 * result + (points ?: 0)
        return result
    }
}

@Service
interface TableService {
    fun updateTable(team: Team): IntArray
    fun findByTeam(teamId: Int): Table
}

@Service
@Transactional
open class TableServiceImpl
@Autowired constructor(private val tableServiceController: TableServiceController) : TableService {
    override fun updateTable(team: Team): IntArray {
        return tableServiceController.updateTable(team)
    }

    override fun findByTeam(teamId: Int): Table {
        return tableServiceController.findByTeam(teamId)
    }
}

@Component
open class TableServiceController @Autowired constructor(private val dsl: DSLContext) {
    fun updateTable(team: Team): IntArray {
        deleteTable(team.id)
        return createTable(team)
    }

    fun findByTeam(teamId: Int): Table {
        val tableTeams = getEntities(dsl.selectFrom(TABLE).where(TABLE.TEAM_ID.eq(teamId)).fetch())
        return Table(tableTeams, teamId)
    }

    fun deleteTable(teamId: Int) {
        dsl.deleteFrom(TABLE).where(TABLE.TEAM_ID.eq(teamId)).execute()
    }

    fun createTable(team: Team): IntArray {
        val tableFetcher = TableFetcher(team)
        val table = tableFetcher.getTable()

        return dsl.batchInsert(createTableTeamRecords(table, team)).execute()
    }

    private fun getEntities(result: Result<TableRecord>): List<TableTeam> {
        val allTeams = mutableListOf<TableTeam>()
        result.forEach { getEntity(it)?.let { allTeams.add(it) } }

        return allTeams
    }

    private fun getEntity(tableRecord: TableRecord?): TableTeam? {
        tableRecord?.let { return TableTeam(it) }
        return null
    }

    private fun createTableTeamRecords(table: com.pengelkes.service.Table?, team: Team): List<TableRecord> {
        val tableTeamRecords = mutableListOf<TableRecord>()

        table?.let {
            it.tableTeams.forEach {
                val tableTeamRecord = TableRecord()
                tableTeamRecord.position = it.position
                tableTeamRecord.name = it.name
                tableTeamRecord.icon = it.icon
                tableTeamRecord.games = it.games
                tableTeamRecord.wonGames = it.wonGames
                tableTeamRecord.tiedGames = it.tiedGames
                tableTeamRecord.lostGames = it.lostGames
                tableTeamRecord.goalRatio = it.goalRatio
                tableTeamRecord.goalDifference = it.goalDifference
                tableTeamRecord.points = it.points
                tableTeamRecord.teamId = team.id

                tableTeamRecords.add(tableTeamRecord)
            }
        }

        return tableTeamRecords
    }
}

