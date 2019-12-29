package com.fullsekurity.theatreblood.repository.network

import android.util.Log
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

internal class DonorsJsonDeserializer : JsonDeserializer<Any> {

    private val TAG = DonorsJsonDeserializer::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        // Jim Morris, 12/9/2019
        // This code does not appear to ever execute, although it did while I was using Retrofit and OkHttp callbacks
        // It stopped executing when I added the line
        //     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // to APIClient, which was added for the purpose of using RxJava calls for Retrofit and OkHttp, instead of callbacks
        var donors: ArrayList<Donor>? = null
        try {
            val jsonObject = json.asJsonObject
            val donorsJsonArray = jsonObject.getAsJsonArray(Constants.MOVIES_ARRAY_DATA_TAG)
            donors = ArrayList(donorsJsonArray.size())
            for (i in 0 until donorsJsonArray.size()) {
                val dematerialized = context.deserialize<Any>(donorsJsonArray.get(i), Donor::class.java)
                donors?.add(dematerialized as Donor)
            }
        } catch (e: JsonParseException) {
            Log.e(TAG, String.format("Could not deserialize Donor element: %s", json.toString()))
        }

        return donors
    }

}