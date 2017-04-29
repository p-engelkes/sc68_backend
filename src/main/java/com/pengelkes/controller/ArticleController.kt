package com.pengelkes.controller

import com.pengelkes.service.Article
import com.pengelkes.service.ArticleService
import com.pengelkes.service.Team
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

    @RequestMapping(value = "/articles/distinct/team", method = arrayOf(RequestMethod.GET))
    fun getDistinctTeamsWithAnArticle(): List<Team> = articleService.getDistinctTeamsWithAnArticle()

    @RequestMapping(value = "/articles", method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody article: Article): Article = articleService.create(article)

    @RequestMapping(value = "/articles/{id}", method = arrayOf(RequestMethod.POST))
    fun update(@RequestBody article: Article, @PathVariable id: Int): Article = articleService.update(article)

    @RequestMapping(value = "/articles/{id}", method = arrayOf(RequestMethod.GET))
    fun findById(@PathVariable id: Int): Article? = articleService.findById(id);

    @RequestMapping(value = "/articles/filter", method = arrayOf(RequestMethod.GET))
    fun findByFilter(@RequestParam("authorId", required = false) authorId: Int?,
                     @RequestParam("teamId", required = false) teamId: Int?): List<Article> {
        if (authorId == null && teamId == null) {
            return articleService.findAll()
        }

        authorId?.let { return articleService.findByAuthor(authorId) }
        teamId?.let { return articleService.findByTeam(teamId) }

        return emptyList()
    }
}