package com.pengelkes.controller

import com.pengelkes.service.OldClass
import com.pengelkes.service.OldClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by pengelkes on 10.03.2017.
 */
@RestController
@RequestMapping("/api")
class OldClassController @Autowired constructor(val oldClassService: OldClassService) {
    @RequestMapping(value = "/oldClasses", method = arrayOf(RequestMethod.GET))
    fun findAllWithTeamsAndArticles(@RequestParam("articles", required = false) articles: Boolean?,
                                    @RequestParam("teams", required = false) teams: Boolean?): List<OldClass> {
        if (articles != null && articles) {
            return oldClassService.findAllWithTeamsAndArticles()
        } else if (teams != null && teams) {
            return oldClassService.findAllWithTeams()
        } else {
            return oldClassService.findAll();
        }
    }
}