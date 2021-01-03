package com.jengger.githubuserapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItems(
    var photo: String = "",
    var username: String = "",
    var name: String = "",
    var location: String = "",
    var company: String = "",
    var followers: String = "",
    var following: String = "",
    var repository: String = "",
    var urlFollowers: String = "",
    var urlFollowing: String = ""
) : Parcelable

