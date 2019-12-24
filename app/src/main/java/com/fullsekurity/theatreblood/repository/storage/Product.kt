package com.fullsekurity.theatreblood.repository.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Product(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") @SerializedName(value = "title") var title: String = "",
    @ColumnInfo(name = "poster_path") @SerializedName(value = "poster_path") var posterPath: String = "",
    @ColumnInfo(name = "original_language") @SerializedName(value = "original_language") var originalLanguage: String = "",
    @ColumnInfo(name = "release_date") @SerializedName(value = "release_date") var releaseDate: String = ""

)