package com.pengelkes.service

import com.pengelkes.backend.jooq.tables.UserProfilePicture.USER_PROFILE_PICTURE
import com.pengelkes.backend.jooq.tables.records.UserProfilePictureRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by patrickengelkes on 03/01/2017.
 */
class ProfilePicture {
    var picture: String? = null
    var width: Int? = null
    var height: Int? = null
    var ratio: Float? = null
    var createdAt: Date? = null
    var userId: Int? = null
    var user: User? = null

    constructor(profilePictureRecord: UserProfilePictureRecord) {
        this.picture = profilePictureRecord.picture
        this.width = profilePictureRecord.width
        this.height = profilePictureRecord.height
        this.ratio = profilePictureRecord.ratio
        this.createdAt = profilePictureRecord.createdAt
    }

    constructor(userId: Int, picture: String, width: Int, height: Int, ratio: Float) {
        this.userId = userId
        this.picture = picture
        this.width = width
        this.height = height
        this.ratio = ratio
    }
}

@Service
interface ProfilePictureService {
    fun add(profilePicture: ProfilePicture): ProfilePicture
    fun findById(userId: Int): ProfilePicture?
}

@Service
@Transactional
open class ProfilePictureServiceImpl
@Autowired constructor(private val profilePictureServiceController: ProfilePictureServiceController)
    : ProfilePictureService {

    override fun add(profilePicture: ProfilePicture) = profilePictureServiceController.add(profilePicture)
    override fun findById(userId: Int) = profilePictureServiceController.findById(userId)
}

@Component
open class ProfilePictureServiceController @Autowired constructor(val dsl: DSLContext) {
    fun add(profilePicture: ProfilePicture): ProfilePicture {
        if (findById(profilePicture.userId) == null) {
            //update profile picture
            dsl.insertInto(USER_PROFILE_PICTURE)
                    .set(USER_PROFILE_PICTURE.USER_ID, profilePicture.userId)
                    .set(USER_PROFILE_PICTURE.PICTURE, profilePicture.picture)
                    .set(USER_PROFILE_PICTURE.HEIGHT, profilePicture.height)
                    .set(USER_PROFILE_PICTURE.WIDTH, profilePicture.width)
                    .set(USER_PROFILE_PICTURE.RATIO, profilePicture.ratio)
                    .execute()
        } else {
            //create profile picture
            dsl.update(USER_PROFILE_PICTURE)
                    .set(USER_PROFILE_PICTURE.PICTURE, profilePicture.picture)
                    .set(USER_PROFILE_PICTURE.HEIGHT, profilePicture.height)
                    .set(USER_PROFILE_PICTURE.WIDTH, profilePicture.width)
                    .set(USER_PROFILE_PICTURE.RATIO, profilePicture.ratio)
                    .execute()
        }

        return profilePicture
    }

    fun findById(userId: Int?): ProfilePicture? {
        if (userId != null) {
            return getEntity(dsl.selectFrom(USER_PROFILE_PICTURE)
                    .where(USER_PROFILE_PICTURE.USER_ID.eq(userId)).fetchOne())
        }

        return null
    }

    private fun getEntity(profilePictureRecord: UserProfilePictureRecord?): ProfilePicture? {
        if (profilePictureRecord != null) {
            return ProfilePicture(profilePictureRecord)
        }

        return null
    }
}

