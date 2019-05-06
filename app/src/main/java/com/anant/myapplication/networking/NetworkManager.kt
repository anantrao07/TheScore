package com.anant.myapplication.networking

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.anant.myapplication.persistance.storeTeamItems
import com.anant.myapplication.response.GetAllTeamInfo
import com.anant.myapplication.response.Route
import com.anant.myapplication.response.parseTeamItems
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.android.HandlerContext
import kotlinx.coroutines.experimental.run
import okhttp3.*
import java.io.IOException
import java.net.URI

class API private constructor(var baseURL: URI) {
    companion object {
        val baseRoute: API =
            API(URI.create("https://raw.githubusercontent.com"))
    }
}

object NetworkManager {
    var api = API.baseRoute
    private const val TAG = "NetworkManager"
    private val networkRequestContext: HandlerContext
    private val networkRequestHandlerThread = HandlerThread("request.looper")

    init {
        networkRequestHandlerThread.start()
        networkRequestContext =
            HandlerContext(Handler(networkRequestHandlerThread.looper), "request.networkRequestContext")
    }

    private suspend fun <T> suspendRequest(
        route: Route,
        parse: (body: ResponseBody) -> T,
        store: (T) -> Unit
    ): T = kotlinx.coroutines.experimental.run(networkRequestContext)
    {
        val result: T?
        try {
            // fetch
            val responseBody = suspendRequest(route)

            // parse
            val parsedResponse = parse(responseBody)

            // store
            store(parsedResponse)

            Log.d("Success:", route.path)

            result = parsedResponse
        } catch (e: Exception) {
            Log.d("failure:", route.path)
            Log.d(TAG, e.localizedMessage)
            Log.d(TAG, e.printStackTrace().toString())
            throw e
        } catch (e: Exception) {
            Log.d("failure:", route.path)
            Log.e(TAG, e.localizedMessage)
            throw e
        }
        result!!
    }

    private suspend fun suspendRequest(route: Route): ResponseBody =
        run(networkRequestContext) {
            var responseBody: ResponseBody?

            try {
                val request = route.makeRequest(api)
                responseBody = OkHttpClient().newCall(request).suspendEnqueue()
            } catch (e: Exception) {
                e.localizedMessage
                throw e
            }
            responseBody!!
        }

    private suspend fun Call.suspendEnqueue(): ResponseBody {
        val deferred = CompletableDeferred<ResponseBody>()

        this.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()
                if (body != null) {
                    if (!response.isSuccessful) {
                        deferred.completeExceptionally(RequestException(response.message(), response.code()))
                    } else {
                        deferred.complete(body)
                    }
                } else {
                    deferred.completeExceptionally(RequestException("Missing response body", -1))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                deferred.completeExceptionally(e)
            }
        })

        return deferred.await()
    }

    suspend fun suspendRequestTeamItems() {
            suspendRequest(GetAllTeamInfo(), ::parseTeamItems, ::storeTeamItems)
    }
}

class RequestException(reason: String, val code: Int) : RuntimeException(reason)