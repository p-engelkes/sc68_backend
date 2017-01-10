package com.pengelkes.controller

import com.pengelkes.DatabaseTestCase
import com.pengelkes.service.*
import com.winterbe.expekt.should
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by patrickengelkes on 10/01/2017.
 */
class ArticleTest : DatabaseTestCase() {

    companion object {
        val user = User("test@test.com", "test")
        val team = Team("1. Mannschaft", hashMapOf())
        val article = Article(
                "Test Title",
                "Test Content"
        )
    }

    @Before
    override fun setup() {
        super.setup()
        article.authorId = null
        article.teamId = null
    }

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var teamService: TeamService

    @Test
    fun testCreate() {
        articleService.create(article)
        articleService.findAll().size.should.equal(1)
    }

    @Test
    fun testFindAll() {
        articleService.findAll().size.should.equal(0)
    }

    @Test
    fun testFindByAuthor() {
        val authorId = userService.registerNewUser(user)
        article.authorId = authorId
        articleService.create(article)
        articleService.findByAuthor(authorId).should.not.be.`null`
    }
}
