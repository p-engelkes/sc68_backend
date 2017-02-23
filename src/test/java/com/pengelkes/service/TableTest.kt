package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 19.02.2017.
 */
class TableTest : SpringTestCase() {

    @Autowired
    lateinit var tableService: TableService

    @Test
    fun testGetByTeam() {
        tableService.findByTeam(databaseTeamOne.id).tableTeams.size.should.equal(4)
    }

    @Test
    fun testUpdateTableByTeamWithNoValue() {
        databaseTeamOne.soccerInfoId = null
        tableService.updateTable(databaseTeamOne).size.should.equal(0)
    }

    @Test
    fun testUpdateTableByTeamWithValues() {
        tableService.updateTable(databaseTeamOne).size.should.equal(14)
    }
}