package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.OldClasses.OLD_CLASSES
import com.pengelkes.backend.jooq.tables.records.OldClassesRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by pengelkes on 09.03.2017.
 */
class OldClass {

    var id: Int? = null
    var name: String? = null
    var orderNumber: Int? = null
    var teams: List<Team> = listOf()

    constructor()

    constructor(id: Int? = null, name: String? = null, orderNumber: Int? = null) {
        this.id = id
        this.name = name
        this.orderNumber = orderNumber
    }

    constructor(oldClassRecord: OldClassesRecord) {
        this.id = oldClassRecord.id
        this.name = oldClassRecord.name
        this.orderNumber = oldClassRecord.orderNumber
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as OldClass

        if (id != other.id) return false
        if (name != other.name) return false
        if (orderNumber != other.orderNumber) return false
        if (teams != other.teams) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (orderNumber ?: 0)
        result = 31 * result + teams.hashCode()
        return result
    }
}

@Service
interface OldClassService {
    fun findAll(): List<OldClass>
    fun findAllWithTeams(): List<OldClass>
    fun findAllWithTeamsAndArticles(): List<OldClass>
}

@Service
@Transactional
open class OldClassServiceImpl
@Autowired
constructor(private val oldClassServiceController: OldClassServiceController) : OldClassService {
    override fun findAll(): List<OldClass> = oldClassServiceController.findAll()

    override fun findAllWithTeams(): List<OldClass> = oldClassServiceController.findAllWithTeams()

    override fun findAllWithTeamsAndArticles(): List<OldClass> = oldClassServiceController.findAllWithTeamsAndArticles()
}

@Component
open class OldClassServiceController
@Autowired
constructor(private val dsl: DSLContext, private val teamService: TeamService) {
    fun findAll(): List<OldClass> {
        return getEntities(dsl.selectFrom(OLD_CLASSES)
                .orderBy(OLD_CLASSES.ORDER_NUMBER.asc())
                .fetch()
        )
    }

    fun findAllWithTeams(): List<OldClass> {
        val allOldClasses = getEntities(dsl.selectFrom(OLD_CLASSES)
                .orderBy(OLD_CLASSES.ORDER_NUMBER.asc())
                .fetch())

        allOldClasses.forEach { it.teams = teamService.findByOldClass(it.id!!) }

        return allOldClasses.filter { it.teams.isNotEmpty() }
    }

    fun findAllWithTeamsAndArticles(): List<OldClass> {
        val allOldClasses = getEntities(dsl.selectFrom(OLD_CLASSES)
                .orderBy(OLD_CLASSES.ORDER_NUMBER.asc())
                .fetch())

        allOldClasses.forEach {
            it.teams = teamService.findByOldClassWithArticle(it.id!!)
        }

        return allOldClasses.filter { it.teams.isNotEmpty() }
    }

    private fun getEntity(oldClassRecord: OldClassesRecord?): OldClass? {
        if (oldClassRecord != null) {
            return OldClass(oldClassRecord)
        }

        return null
    }

    private fun getEntities(result: Result<OldClassesRecord>): List<OldClass> {
        val allOldClasses = mutableListOf<OldClass>()
        result.forEach { getEntity(it)?.let { allOldClasses.add(it) } }

        return allOldClasses
    }
}