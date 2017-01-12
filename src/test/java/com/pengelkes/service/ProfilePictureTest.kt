package com.pengelkes.service

import com.pengelkes.SpringTestCase
import com.winterbe.expekt.should
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by pengelkes on 11.01.2017.
 */
class ProfilePictureTest : SpringTestCase() {

    companion object {
        val userId = 1
    }

    @Autowired
    lateinit var profilePictureService: ProfilePictureService

    //TODO: Create a test with another profile pictuure and check width heigth and ratio

    @Test
    fun testFindById() {
        profilePictureService.findById(userId).should.not.be.`null`
    }
}