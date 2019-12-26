package com.fullsekurity.theatreblood.repository.storage

import androidx.room.*
import com.fullsekurity.theatreblood.donors.Donor
import io.reactivex.Single


@Dao
interface DBDao {

    // Donor
    @get:Query("SELECT * FROM donors")
    val donors: List<Donor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDonor(donor: Donor)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocalDonor(donor: Donor)

    @Query("DELETE FROM donors")
    fun deleteAllDonors()

    @Query("SELECT COUNT(title) FROM donors")
    fun getEntryCount(): Single<Int>

    @Update
    fun updateDonor(donor: Donor)

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullName(searchLast: String, searchFirst :String) : Single<List<Donor>>

    // Product

//    @Transaction
//    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
//    fun getDonorAndAllProducts(searchLast: String, searchFirst :String): Single<List<DonorAndAllProducts>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertProducts(products: List<Product>)

}