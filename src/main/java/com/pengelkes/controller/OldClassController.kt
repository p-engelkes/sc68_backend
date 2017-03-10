package com.pengelkes.controller

import com.pengelkes.service.OldClass
import com.pengelkes.service.OldClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by pengelkes on 10.03.2017.
 */
@RestController
@RequestMapping("/api")
class OldClassController @Autowired constructor(val oldClassService: OldClassService) {
    @RequestMapping(value = "/oldClasses", method = arrayOf(RequestMethod.GET))
    fun findAll(): List<OldClass> {
        return oldClassService.findAll()
    }
}