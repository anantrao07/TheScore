package com.anant.myapplication.persistance

import com.anant.myapplication.data.TeamItems
import com.anant.myapplication.utils.SortOptions
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

fun storeTeamItems(teamItems: List<TeamItems>) {
    storeOrUpdate(teamItems)
}

fun storeOrUpdate(entities: List<RealmObject>) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction {
        it.copyToRealmOrUpdate(entities)
    }
    realm.close()
}

fun fetchTeamItems(realm: Realm): RealmResults<TeamItems>? {
    return realm.where(TeamItems::class.java)
        .findAll()
}

fun fetchSortedTeamItems(realm: Realm, fieldName: SortOptions): RealmResults<TeamItems>? {
    return realm.where(TeamItems::class.java)
        .findAll().sort(fieldName.sortBy)
}

fun fetchTeam(realm: Realm, teamId: Int): TeamItems? {
    return realm.where(TeamItems::class.java).
        equalTo("TeamId", teamId).findFirst()
}
