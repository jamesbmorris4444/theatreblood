package com.greendot.rewards.repository

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "moviesObservable")
data class Movie(

    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "vote_count") @SerializedName(value = "vote_count") var voteCount: Int,
    @ColumnInfo(name = "video") @SerializedName(value = "video") var video: Boolean,
    @ColumnInfo(name = "vote_average") @SerializedName(value = "vote_average") var voteAverage: Float,
    @ColumnInfo(name = "title") @SerializedName(value = "title") var title: String,
    @ColumnInfo(name = "popularity") @SerializedName(value = "popularity") var popularity: Float,
    @ColumnInfo(name = "poster_path") @SerializedName(value = "poster_path") var posterPath: String,
    @ColumnInfo(name = "original_language") @SerializedName(value = "original_language") var originalLanguage: String,
    @ColumnInfo(name = "original_title") @SerializedName(value = "original_title") var originalTitle: String,
    @ColumnInfo(name = "backdrop_path") @SerializedName(value = "backdrop_path") var backdropPath: String,
    @ColumnInfo(name = "adult") @SerializedName(value = "adult") var adult: Boolean,
    @ColumnInfo(name = "overview") @SerializedName(value = "overview") var overview: String,
    @ColumnInfo(name = "release_date") @SerializedName(value = "release_date") var releaseDate: String

) {
    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}