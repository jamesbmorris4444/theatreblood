package com.fullsekurity.theatreblood.repository.network

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.API
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
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(API), String.format("okHttp logging interceptor=%s", message))
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
            jsonObject.remove("results")
            jsonObject.put("results", createListOfDonors(20))
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
            val aboRh: String  = when (index) {
                0 -> { "O-Positive" }
                1 -> { "O-Negative" }
                2 -> { "A-Positive" }
                3 -> { "A-Negative" }
                4 -> { "B-Positive" }
                5 -> { "B-Negative" }
                6 -> { "AB-Positive" }
                7 -> { "AB-Negative" }
                8 -> { "O-Positive" }
                9 -> { "O-Negative" }
                10 -> { "A-Positive" }
                11 -> { "A-Negative" }
                12 -> { "B-Positive" }
                13 -> { "B-Negative" }
                14 -> { "AB-Positive" }
                15 -> { "AB-Negative" }
                16 -> { "O-Positive" }
                17 -> { "O-Negative" }
                18 -> { "A-Positive" }
                19 -> { "A-Negative" }
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

    private fun createListOfDonors(donors: Int) : JSONArray {
        val jsonTopArray = JSONArray()
        for (index in 0 until donors) {
            val lastName: String  = when (index) {
                0 -> { "Morris01" }
                1 -> { "Smith01" }
                2 -> { "Taylor01" }
                3 -> { "Lewis01" }
                4 -> { "Snowdon01" }
                5 -> { "Miller01" }
                6 -> { "Jones01" }
                7 -> { "Johnson01" }
                8 -> { "Early01" }
                9 -> { "Wynn01" }
                10 -> { "Morris02" }
                11 -> { "Smith02" }
                12 -> { "Taylor02" }
                13 -> { "Lewis02" }
                14 -> { "Snowdon02" }
                15 -> { "Miller02" }
                16 -> { "Jones02" }
                17 -> { "Johnson02" }
                18 -> { "Early02" }
                19 -> { "Wynn02" }
                else -> { "" }
            }
            val firstName: String  = when (index) {
                0 -> { "FirstMorris01" }
                1 -> { "FirstSmith01" }
                2 -> { "FirstTaylor01" }
                3 -> { "FirstLewis01" }
                4 -> { "FirstSnowdon01" }
                5 -> { "FirstMiller01" }
                6 -> { "FirstJones01" }
                7 -> { "FirstJohnson01" }
                8 -> { "FirstEarly01" }
                9 -> { "FirstWynn01" }
                10 -> { "FirstMorris02" }
                11 -> { "FirstSmith02" }
                12 -> { "FirstTaylor02" }
                13 -> { "FirstLewis02" }
                14 -> { "FirstSnowdon02" }
                15 -> { "FirstMiller02" }
                16 -> { "FirstJones02" }
                17 -> { "FirstJohnson02" }
                18 -> { "FirstEarly02" }
                19 -> { "FirstWynn02" }
                else -> { "" }
            }
            val middleName: String  = when (index) {
                0 -> { "MiddleMorris01" }
                1 -> { "MiddleSmith01" }
                2 -> { "MiddleTaylor01" }
                3 -> { "MiddleLewis01" }
                4 -> { "MiddleSnowdon01" }
                5 -> { "MiddleMiller01" }
                6 -> { "MiddleJones01" }
                7 -> { "MiddleJohnson01" }
                8 -> { "MiddleEarly01" }
                9 -> { "MiddleWynn01" }
                10 -> { "MiddleMorris02" }
                11 -> { "MiddleSmith02" }
                12 -> { "MiddleTaylor02" }
                13 -> { "MiddleLewis02" }
                14 -> { "MiddleSnowdon02" }
                15 -> { "MiddleMiller02" }
                16 -> { "MiddleJones02" }
                17 -> { "MiddleJohnson02" }
                18 -> { "MiddleEarly02" }
                19 -> { "MiddleWynn02" }
                else -> { "" }
            }
            val aboRh: String  = when (index) {
                0 -> { "O-Positive" }
                1 -> { "O-Negative" }
                2 -> { "A-Positive" }
                3 -> { "A-Negative" }
                4 -> { "B-Positive" }
                5 -> { "B-Negative" }
                6 -> { "AB-Positive" }
                7 -> { "AB-Negative" }
                8 -> { "O-Positive" }
                9 -> { "O-Negative" }
                10 -> { "A-Positive" }
                11 -> { "A-Negative" }
                12 -> { "B-Positive" }
                13 -> { "B-Negative" }
                14 -> { "AB-Positive" }
                15 -> { "AB-Negative" }
                16 -> { "O-Positive" }
                17 -> { "O-Negative" }
                18 -> { "A-Positive" }
                19 -> { "A-Negative" }
                else -> { "" }
            }
            val dob: String  = when (index) {
                0 -> { "01 Jan 1995" }
                1 -> { "02 Jan 1995" }
                2 -> { "03 Jan 1995" }
                3 -> { "04 Jan 1995" }
                4 -> { "05 Jan 1995" }
                5 -> { "06 Jan 1995" }
                6 -> { "07 Jan 1995" }
                7 -> { "08 Jan 1995" }
                8 -> { "09 Jan 1995" }
                9 -> { "10 Jan 1995" }
                10 -> { "11 Jan 1995" }
                11 -> { "12 Jan 1995" }
                12 -> { "13 Jan 1995" }
                13 -> { "14 Jan 1995" }
                14 -> { "15 Jan 1995" }
                15 -> { "16 Jan 1995" }
                16 -> { "17 Jan 1995" }
                17 -> { "18 Jan 1995" }
                18 -> { "19 Jan 1995" }
                19 -> { "20 Jan 1995" }
                else -> { "" }
            }
            val branch: String  = when (index) {
                0 -> { "The Army" }
                1 -> { "The Army" }
                2 -> { "The Army" }
                3 -> { "The Army" }
                4 -> { "The Army" }
                5 -> { "The Army" }
                6 -> { "The Marines" }
                7 -> { "The Marines" }
                8 -> { "The Marines" }
                9 -> { "The Marines" }
                10 -> { "The Navy" }
                11 -> { "The Navy" }
                12 -> { "The Navy" }
                13 -> { "The Navy" }
                14 -> { "The Navy" }
                15 -> { "The Air Force" }
                16 -> { "The Air Force" }
                17 -> { "The Air Force" }
                18 -> { "The JCS" }
                19 -> { "The JCS" }
                else -> { "" }
            }
            val jsonObject = JSONObject()
            jsonObject.put("vote_count", 5)
            jsonObject.put("video", false)
            jsonObject.put("vote_average", 10f)
            jsonObject.put("title", lastName)

            jsonObject.put("popularity", 20f)
            jsonObject.put("poster_path", firstName)
            jsonObject.put("original_language", middleName)
            jsonObject.put("original_title", branch)

            jsonObject.put("backdrop_path", aboRh)
            jsonObject.put("adult", true)
            jsonObject.put("overview", "NOT USED")
            jsonObject.put("release_date", dob)
            jsonTopArray.put(jsonObject)
        }
        return jsonTopArray
    }

}
