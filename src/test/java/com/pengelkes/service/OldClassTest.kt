package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 10.03.2017.
 */
class OldClassTest : SpringTestCase() {

    @Autowired
    lateinit var oldClassService: OldClassService

    @Test
    @Ignore
    fun testFindAllWithTeams() {
        val expected = listOf(oldClassOne, oldClassTwo)

        oldClassService.findAllWithTeams().should.equal(expected)
    }

    @Test
    @Ignore
    fun testFindAllWithTeamsAndArticles() {
        val expected = listOf(oldClassOne)

        oldClassService.findAllWithTeamsAndArticles().should.equal(expected)
    }
}