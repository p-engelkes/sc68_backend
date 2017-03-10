package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 10.03.2017.
 */
class OldClassTest : SpringTestCase() {

    @Autowired
    lateinit var oldClassService: OldClassService

    @Test
    fun testFindAll() {
        val expected = listOf(oldClassOne, oldClassTwo)

        oldClassService.findAll().should.equal(expected)
    }
}