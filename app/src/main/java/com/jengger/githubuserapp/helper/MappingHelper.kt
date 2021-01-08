package com.jengger.githubuserapp.helper

import android.database.Cursor
import com.jengger.githubuserapp.Model.UserItems
import com.jengger.githubuserapp.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserItems> {
        val usersList = ArrayList<UserItems>()
        usersCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPO))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val urlFollowers = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL_FOLLOWING))
                val urlFollowing = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL_FOLLOWERS))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.IMAGE_USER))
                usersList.add(
                        UserItems(photo = photo,
                                username = username,
                                name = name,
                                company = company,
                                location = location,
                                repository = repository,
                                followers = followers,
                                following = following,
                                urlFollowing = urlFollowing,
                                urlFollowers = urlFollowers
                        )
                )
            }
        }
        return usersList
    }
}