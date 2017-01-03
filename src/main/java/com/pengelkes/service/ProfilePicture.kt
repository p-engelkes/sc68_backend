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
    var id: Int = 0;
    var picture: String? = null
    var width: Int? = null
    var height: Int? = null
    var ratio: Float? = null
    var createdAt: Date? = null
    var userId: Int? = null
    var user: User? = null

    constructor(profilePictureRecord: UserProfilePictureRecord) {
        this.id = profilePictureRecord.id
        this.picture = profilePictureRecord.picture
        this.width = profilePictureRecord.width
        this.height = profilePictureRecord.height
        this.ratio = profilePictureRecord.ratio
        this.createdAt = profilePictureRecord.createdAt
    }

    constructor(picture: String, width: Int, height: Int, ratio: Float, userId: Int) {
        this.picture = picture
        this.width = width
        this.height = height
        this.ratio = ratio
        this.userId = userId
    }
}

@Service
interface ProfilePictureService {
    fun addProfilePicture(profilePicture: ProfilePicture): ProfilePicture
}

@Service
@Transactional
open class ProfilePictureServiceImpl
@Autowired constructor(private val profilePictureServiceController: ProfilePictureServiceController)
    : ProfilePictureService {
    override fun addProfilePicture(profilePicture: ProfilePicture): ProfilePicture =
            profilePictureServiceController.addProfilePicture(profilePicture)
}

@Component
open class ProfilePictureServiceController @Autowired constructor(val dsl: DSLContext) {
    fun addProfilePicture(profilePicture: ProfilePicture): ProfilePicture {
        val userProfilePictureRecord = dsl.insertInto(USER_PROFILE_PICTURE)
                .set(USER_PROFILE_PICTURE.PICTURE, profilePicture.picture)
                .set(USER_PROFILE_PICTURE.HEIGHT, profilePicture.height)
                .set(USER_PROFILE_PICTURE.WIDTH, profilePicture.width)
                .set(USER_PROFILE_PICTURE.RATIO, profilePicture.ratio)
                .returning(USER_PROFILE_PICTURE.ID)
                .fetchOne()

        profilePicture.id = userProfilePictureRecord.id
        return profilePicture
    }
}

