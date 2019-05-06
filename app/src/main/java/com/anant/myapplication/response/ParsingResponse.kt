package com.anant.myapplication.response

import android.util.Log
import com.anant.myapplication.data.TeamItems
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody


class ParseException(reason: String): RuntimeException(reason)

private const val TAG = "RESPONSEPARSING"
fun parseTeamItems(responseBody: ResponseBody): List<TeamItems> {

    val responseString = responseBody.string()

    val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    try {
        val teamItemsListData = gson.fromJson(responseString, Array<TeamItems>::class.java).toList()

        Log.d("teamResponse", teamItemsListData[0].wins.toString())
        return teamItemsListData
    } catch (e: Exception) {
        Log.d(TAG, e.localizedMessage)
    }
    throw ParseException("Couldn't parse Teams list ")
}