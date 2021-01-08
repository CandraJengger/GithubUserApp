package com.jengger.githubuserapp.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val COMPANY = "company"
            const val REPO = "repo"
            const val IMAGE_USER = "image_user"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val URL_FOLLOWERS = "url_followers"
            const val URL_FOLLOWING = "url_following"
        }
    }
}