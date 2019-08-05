package com.greendot.rewards.repository

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.greendot.rewards.Constants
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.logger.LogUtils.TagFilter.ANX
import java.lang.reflect.Type

internal class JsonDeserializer : JsonDeserializer<Any> {

    // https://api.themoviedb.org/3/discover/movie?api_key=17c5889b399d3c051099e4098ad83493&language=en-US&page=1
    //
    //     returns JSON as follows:
    //
    // {
    //  "page": 1,
    //  "total_results": 428626,
    //  "total_pages": 21432,
    //  "results": [
    //    {
    //      "vote_count": 1041,
    //      "id": 420818,
    //      "video": false,
    //      "vote_average": 7.2,
    //      "title": "The Lion King",
    //      "popularity": 539.456,
    //      "poster_path": "/dzBtMocZuJbjLOXvrl4zGYigDzh.jpg",
    //      "original_language": "en",
    //      "original_title": "The Lion King",
    //      "genre_ids": [
    //        12,
    //        16,
    //        10751,
    //        18,
    //        28
    //      ],
    //      "backdrop_path": "/1TUg5pO1VZ4B0Q1amk3OlXvlpXV.jpg",
    //      "adult": false,
    //      "overview": "Simba idolises his father, King Mufasa, and takes to heart his own royal destiny. But not everyone in the kingdom celebrates the new cub's arrival. Scar, Mufasa's brother—and former heir to the throne—has plans of his own. The battle for Pride Rock is ravaged with betrayal, tragedy and drama, ultimately resulting in Simba's exile. With help from a curious pair of newfound friends, Simba will have to figure out how to grow up and take back what is rightfully his.",
    //      "release_date": "2019-07-12"
    //    },
    //   ...
    //  ]
    // }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        var movies: ArrayList<Movie>? = null
        try {
            // get the full JSON object in jsonObject
            val jsonObject = json.asJsonObject
            // get the inner "results" array in moviesJsonArray
            val moviesJsonArray = jsonObject.getAsJsonArray(Constants.MOVIES_ARRAY_DATA_TAG)
            // get the inner array into an object of type ArrayList<Movie> so it can be returned
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