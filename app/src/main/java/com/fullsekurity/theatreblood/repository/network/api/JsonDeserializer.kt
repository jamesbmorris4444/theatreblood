package com.fullsekurity.theatreblood.repository.network.api

import android.util.Log
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

internal class MoviesJsonDeserializer : JsonDeserializer<Any> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        var movies: java.util.ArrayList<Movie>? = null
        try {
            val jsonObject = json.asJsonObject
            val moviesJsonArray = jsonObject.getAsJsonArray(Constants.MOVIES_ARRAY_DATA_TAG)
            movies = ArrayList<Movie>(moviesJsonArray.size())
            for (i in 0 until moviesJsonArray.size()) {
                // adding the converted wrapper to our container
                val dematerialized = context.deserialize<Any>(moviesJsonArray.get(i), Movie::class.java)
                movies?.add(dematerialized as Movie)
            }
        } catch (e: JsonParseException) {
            Log.e(TAG, String.format("Could not deserialize Movie element: %s", json.toString()))
        }

        return movies
    }

    companion object {

        private val TAG = MoviesJsonDeserializer::class.java.simpleName
    }
}