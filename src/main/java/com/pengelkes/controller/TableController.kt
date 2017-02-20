package com.pengelkes.controller

import com.pengelkes.service.Table
import com.pengelkes.service.TableService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by pengelkes on 20.02.2017.
 */
@RestController
@RequestMapping("/api")
class TableController @Autowired constructor(val tableService: TableService) {
    @RequestMapping("table", method = arrayOf(RequestMethod.GET))
    fun findByTeam(@RequestParam("teamId", required = true) teamId: Int): Table {
        return tableService.findByTeam(teamId)
    }
}