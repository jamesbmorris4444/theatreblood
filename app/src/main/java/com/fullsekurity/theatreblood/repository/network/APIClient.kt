package com.fullsekurity.theatreblood.repository.network

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.utils.Constants.DONOR_LIST_CLASS_TYPE
import com.fullsekurity.theatreblood.utils.Constants.THEATRE_BLOOD_BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*

object APIClient {
    val client: APIInterface
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("okHttp logging interceptor=%s", message))
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BASIC  // or BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(TransformInterceptor())
                .build()
            val gson = GsonBuilder()
                .registerTypeAdapter(DONOR_LIST_CLASS_TYPE,
                    DonorsJsonDeserializer()
                )
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(THEATRE_BLOOD_BASE_URL)
            return builder.build().create(APIInterface::class.java)
        }

}

class TransformInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val stringJson = response.body?.string()
        return if (stringJson == null) {
            response
        } else {
            val jsonObject = JSONObject(stringJson)
            jsonObject.put("products", createListOfProducts(20))
            val contentType = response.body?.contentType()
            val body: ResponseBody = ResponseBody.create(contentType, jsonObject.toString())
            response.newBuilder().body(body).build()
        }
    }

    private fun createListOfProducts(donors: Int) : JSONArray {
        val random = Random()
        val jsonTopArray = JSONArray()
        for (index in 0 until donors) {
            val productCount = random.nextInt(4)
            val din = random.nextInt(1000).toString()
            val aboRh: String  = when (random.nextInt(6)) {
                0 -> { "O-Positive" }
                1 -> { "O-Negative" }
                2 -> { "A-Positive" }
                3 -> { "A-Negative" }
                4 -> { "B-Positive" }
                5 -> { "B-Negative" }
                else -> { "" }
            }
            val productCode = (random.nextInt(10000) + 9990000).toString()
            val jsonSubArray = JSONArray()
            for (productIndex in 0 until productCount) {
                val jsonObject = JSONObject()
                jsonObject.put("din", din)
                jsonObject.put("abo_rh", aboRh)
                jsonObject.put("product_code", productCode)
                jsonObject.put("expiration_date", "01 Jan 2020")
                jsonSubArray.put(jsonObject)
            }
            jsonTopArray.put(jsonSubArray)
        }
        return jsonTopArray
    }
}
