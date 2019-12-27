package com.fullsekurity.theatreblood.repository.storage

import androidx.room.*
import com.fullsekurity.theatreblood.donors.Donor
import com.fullsekurity.theatreblood.donors.DonorWithProducts
import com.fullsekurity.theatreblood.donors.Product
import io.reactivex.Single


@Dao
interface DBDao {

    // Donor
    @get:Query("SELECT * FROM donors")
    val donors: Single<List<Donor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDonor(donor: Donor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalDonor(donor: Donor)

    @Query("DELETE FROM donors")
    fun deleteAllDonors()

    @Query("SELECT COUNT(id) FROM donors")
    fun getEntryCount(): Int

    @Update
    fun updateDonor(donor: Donor)

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullName(searchLast: String, searchFirst :String) : Single<List<Donor>>

    // Product

    @Transaction
    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun getDonorAndAllProducts(searchLast: String, searchFirst: String): Single<List<DonorWithProducts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalProduct(product: Product)

}