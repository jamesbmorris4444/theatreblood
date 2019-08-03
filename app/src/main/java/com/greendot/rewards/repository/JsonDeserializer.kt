package com.greendot.rewards.repository

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.greendot.rewards.Constants
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.logger.LogUtils.TAG_FILTER.ANX
import java.lang.reflect.Type

internal class JsonDeserializer : JsonDeserializer<Any> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        var movies: java.util.ArrayList<Movie>? = null
        try {
            val jsonObject = json.asJsonObject
            val moviesJsonArray = jsonObject.getAsJsonArray(Constants.MOVIES_ARRAY_DATA_TAG)
            movies = ArrayList(moviesJsonArray.size())
            for (i in 0 until moviesJsonArray.size()) {
                val dematerialized = context.deserialize<Any>(moviesJsonArray.get(i), Movie::class.java)
                movies?.add(dematerialized as Movie)
            }
        } catch (e: JsonParseException) {
            LogUtils.E(LogUtils.FilterTags.withTags(ANX), e)
        }

        return movies
    }

}