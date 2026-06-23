package net.coblos.moodly.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        val responseString = when {
            path.contains("moods") && request.method == "GET" -> "[]"
            path.contains("moods") && request.method == "POST" -> "{}"
            else -> ""
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(responseString.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
