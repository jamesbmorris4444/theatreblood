package com.fullsekurity.theatreblood.repository.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "donorsObservable")
data class Donor(

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

)