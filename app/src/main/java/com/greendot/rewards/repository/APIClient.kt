package com.greendot.rewards.repository

import com.google.gson.GsonBuilder
import com.greendot.rewards.Constants.MOVIE_ARRAY_LIST_CLASS_TYPE
import com.greendot.rewards.Constants.POPULAR_MOVIES_BASE_URL
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.logger.LogUtils.TAG_FILTER.ANX
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    val client: APIInterface
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(ANX), String.format("okHttp logging interceptor=%s", message))
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val gson = GsonBuilder()
                .registerTypeAdapter(MOVIE_ARRAY_LIST_CLASS_TYPE, JsonDeserializer())
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(POPULAR_MOVIES_BASE_URL)
            return builder.build().create(APIInterface::class.java)
        }

}
