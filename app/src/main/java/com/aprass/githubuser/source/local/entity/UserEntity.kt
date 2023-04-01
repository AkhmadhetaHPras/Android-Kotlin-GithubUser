package com.aprass.githubuser.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
class UserEntity(
    @ColumnInfo(name = "login")
    @field:PrimaryKey
    var username: String,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "favorite")
    var favorite: Boolean
) :Parcelable