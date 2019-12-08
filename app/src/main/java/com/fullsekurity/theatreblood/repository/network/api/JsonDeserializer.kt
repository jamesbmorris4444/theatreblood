package com.fullsekurity.theatreblood.repository.network.api

import android.util.Log
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.utils.Constants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

internal class MoviesJsonDeserializer : JsonDeserializer<Any> {

    private val TAG = MoviesJsonDeserializer::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
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