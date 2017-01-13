package com.pengelkes.controller

import com.pengelkes.service.Article
import com.pengelkes.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by pengelkes on 30.12.2016.
 */
@RestController
@RequestMapping("/api")
class ArticleController @Autowired constructor(val articleService: ArticleService) {
    @RequestMapping(value = "/articles", method = arrayOf(RequestMethod.GET))
    fun findAll(): List<Article> = articleService.findAll()

    @RequestMapping(value = "/articles", method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody article: Article): Article = articleService.create(article)

    @RequestMapping(value = "/articles/{id}", method = arrayOf(RequestMethod.POST))
    fun update(@RequestBody article: Article, @PathVariable id: Int): Article = articleService.update(article)

    @RequestMapping(value = "/articles/filter", method = arrayOf(RequestMethod.GET))
    fun findByFilter(@RequestParam("authorId", required = false) authorId: Int?,
                     @RequestParam("teamId", required = false) teamId: Int?): List<Article> {
        authorId?.let { return articleService.findByAuthor(authorId) }
        teamId?.let { return articleService.findByTeam(teamId) }

        return emptyList()
    }
}