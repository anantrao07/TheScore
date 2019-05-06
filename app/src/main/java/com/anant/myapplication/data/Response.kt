package com.anant.myapplication.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TeamItems(
    var wins: Int = 0,
    var losses: Int = 0,
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("id")
    @PrimaryKey
    var TeamId: Int = 0,
    var players: RealmList<Players> = RealmList()
) : RealmObject()

open class Players(
    @SerializedName("id")
    @PrimaryKey
    var playerId: Int = 0,
    @SerializedName("first_name")
    var fullName: String = "",
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("position")
    var playerPosition: String = "",
    var number: Int = 0
) : RealmObject()
