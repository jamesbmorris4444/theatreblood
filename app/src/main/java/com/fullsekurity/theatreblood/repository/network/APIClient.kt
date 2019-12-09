package com.fullsekurity.theatreblood.repository.network

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.utils.Constants.MOVIE_ARRAY_LIST_CLASS_TYPE
import com.fullsekurity.theatreblood.utils.Constants.POPULAR_MOVIES_BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    val client: APIInterface
        get() {

            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("okHttp logging interceptor=%s", message))
                }
            })

            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val gson = GsonBuilder()
                .registerTypeAdapter(MOVIE_ARRAY_LIST_CLASS_TYPE,
                    DonorsJsonDeserializer()
                )
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(POPULAR_MOVIES_BASE_URL)
            return builder.build().create(APIInterface::class.java)
        }

}
