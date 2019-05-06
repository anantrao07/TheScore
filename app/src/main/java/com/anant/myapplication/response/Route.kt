package com.anant.myapplication.response

import com.anant.myapplication.networking.API
import com.anant.myapplication.networking.NetworkManager
import okhttp3.HttpUrl
import okhttp3.Request

sealed class Route(val path: String) {

    open fun getUrl(): HttpUrl {
        return HttpUrl.get(NetworkManager.api.baseURL)!!.newBuilder()
            .addPathSegment(path)
            .build()
    }

    fun makeRequest(api: API): Request {
        val requestMethod = "GET"
        return makeRequest(api, requestMethod)
    }

    private fun makeRequest(api: API, requestMethod: String): Request {

        val requestBuilder = Request.Builder()
            .url(getUrl())
        return requestBuilder.build()
    }
}

class GetAllTeamInfo : Route("/scoremedia/nba-team-viewer/master/input.json") {
    override fun getUrl(): HttpUrl {
        var teamResultInfo = "${NetworkManager.api.baseURL}" +
                "/scoremedia/nba-team-viewer/master/input.json"
        return HttpUrl.parse(teamResultInfo)!!
    }
}