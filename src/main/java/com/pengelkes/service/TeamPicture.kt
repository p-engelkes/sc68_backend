package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.TeamPicture.TEAM_PICTURE
import com.pengelkes.backend.jooq.tables.records.TeamPictureRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by pengelkes on 27.03.2017.
 */
class TeamPicture {
    var id: Int? = null
    var picture: String? = null
    var width: Int? = null
    var height: Int? = null
    var ratio: Float? = null
    var createdAt: Date? = null
    var teamId: Int? = null

    //empty constructor needed for jackson
    constructor()

    constructor(id: Int? = null, picture: String? = null, width: Int? = null, height: Int? = null, ratio: Float? = null,
                createAt: Date? = null, teamId: Int? = null) {
        this.id = id
        this.picture = picture
        this.width = width
        this.height = height
        this.ratio = ratio
        this.createdAt = createdAt
        this.teamId = teamId
    }

    constructor(teamPictureRecord: TeamPictureRecord) {
        this.id = teamPictureRecord.id
        this.picture = teamPictureRecord.picture
        this.width = teamPictureRecord.width
        this.height = teamPictureRecord.height
        this.ratio = teamPictureRecord.ratio
        this.createdAt = teamPictureRecord.createdAt
        this.teamId = teamPictureRecord.teamId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TeamPicture

        if (id != other.id) return false
        if (picture != other.picture) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (ratio != other.ratio) return false
        if (createdAt != other.createdAt) return false
        if (teamId != other.teamId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (picture?.hashCode() ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (height ?: 0)
        result = 31 * result + (ratio?.hashCode() ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (teamId ?: 0)
        return result
    }
}

@Service
interface TeamPictureService {
    fun createByTeam(teamPicture: TeamPicture)
    fun findByTeam(teamId: Int): List<TeamPicture>
}

@Service
@Transactional
open class TeamPictureServiceImpl
@Autowired constructor(private val teamPictureServiceController: TeamPictureServiceController) : TeamPictureService {
    override fun createByTeam(teamPicture: TeamPicture) = teamPictureServiceController.create(teamPicture)

    override fun findByTeam(teamId: Int): List<TeamPicture> = teamPictureServiceController.findByTeam(teamId)

}

@Component
open class TeamPictureServiceController @Autowired constructor(val dsl: DSLContext) {
    fun create(teamPicture: TeamPicture) {
        dsl.insertInto(TEAM_PICTURE)
                .set(TEAM_PICTURE.PICTURE, teamPicture.picture)
                .set(TEAM_PICTURE.WIDTH, teamPicture.width)
                .set(TEAM_PICTURE.HEIGHT, teamPicture.height)
                .set(TEAM_PICTURE.RATIO, teamPicture.ratio)
                .set(TEAM_PICTURE.TEAM_ID, teamPicture.teamId)
                .execute()
    }

    fun findByTeam(teamId: Int?): List<TeamPicture> {
        return getEntities(dsl.selectFrom(TEAM_PICTURE)
                .where(TEAM_PICTURE.TEAM_ID.eq(teamId))
                .fetch())
    }

    private fun getEntities(result: Result<TeamPictureRecord>): List<TeamPicture> {
        val allTeamPictures = mutableListOf<TeamPicture>()
        result.forEach { getEntity(it)?.let { allTeamPictures.add(it) } }

        return allTeamPictures
    }

    private fun getEntity(teamPictureRecord: TeamPictureRecord?): TeamPicture? {
        if (teamPictureRecord != null) {
            return TeamPicture(teamPictureRecord)
        }

        return null
    }
}