package com.fullsekurity.theatreblood.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.fullsekurity.theatreblood.donors.Donor
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products",
    foreignKeys = [
        ForeignKey(entity = Donor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id"),
            onDelete = ForeignKey.CASCADE)
    ])

data class Product(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") @SerializedName(value = "title") var title: String = "",
    @ColumnInfo(name = "poster_path") @SerializedName(value = "poster_path") var posterPath: String = "",
    @ColumnInfo(name = "original_language") @SerializedName(value = "original_language") var originalLanguage: String = "",
    @ColumnInfo(name = "release_date") @SerializedName(value = "release_date") var releaseDate: String = ""

)