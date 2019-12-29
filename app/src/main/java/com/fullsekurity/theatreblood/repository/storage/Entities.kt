package com.fullsekurity.theatreblood.repository.storage

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "donors")
data class Donor(

    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "vote_count") @SerializedName(value = "vote_count") var voteCount: Int = 0,
    @ColumnInfo(name = "video") @SerializedName(value = "video") var video: Boolean = false,
    @ColumnInfo(name = "vote_average") @SerializedName(value = "vote_average") var voteAverage: Float = 0f,
    @ColumnInfo(name = "title") @SerializedName(value = "title") var title: String = "",
    @ColumnInfo(name = "popularity") @SerializedName(value = "popularity") var popularity: Float = 0f,
    @ColumnInfo(name = "poster_path") @SerializedName(value = "poster_path") var posterPath: String = "",
    @ColumnInfo(name = "original_language") @SerializedName(value = "original_language") var originalLanguage: String = "",
    @ColumnInfo(name = "original_title") @SerializedName(value = "original_title") var originalTitle: String = "",
    @ColumnInfo(name = "backdrop_path") @SerializedName(value = "backdrop_path") var backdropPath: String = "",
    @ColumnInfo(name = "adult") @SerializedName(value = "adult") var adult: Boolean = false,
    @ColumnInfo(name = "overview") @SerializedName(value = "overview") var overview: String = "",
    @ColumnInfo(name = "release_date") @SerializedName(value = "release_date") var releaseDate: String = ""

)

@Entity(tableName = "products")
data class Product(

    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "donor_id") var donorId: Long = 0,
    @ColumnInfo(name = "din") @SerializedName(value = "din") var din: String = "",
    @ColumnInfo(name = "abo_rh") @SerializedName(value = "abo_rh") var aboRh: String = "",
    @ColumnInfo(name = "product_code") @SerializedName(value = "product_code") var productCode: String = "",
    @ColumnInfo(name = "expiration_date") @SerializedName(value = "expiration_date") var expirationDate: String = ""

)

data class DonorWithProducts(
    @Embedded val donor: Donor,
    @Relation(
        parentColumn = "id",
        entityColumn = "donor_id"
    )
    val products: List<Product>
)