package com.fullsekurity.theatreblood.repository.storage

import androidx.room.*
import io.reactivex.Single


@Dao
interface DBDao {

    // Donor
//    @get:Query("SELECT * FROM donors")
//    val donors: Single<List<Donor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDonor(donor: Donor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalDonors(donors: List<Donor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalDonor(donor: Donor)

//    @Query("DELETE FROM donors")
//    fun deleteAllDonors()

    @Query("SELECT COUNT(id) FROM donors")
    fun getDonorEntryCount(): Single<Int>

    @Query("SELECT COUNT(id) FROM products")
    fun getProductEntryCount(): Single<Int>

    @Update
    fun updateDonor(donor: Donor)

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM donors")
    fun getAllDonors(): List<Donor>

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullName(searchLast: String, searchFirst :String) : Single<List<Donor>>

    // Product

//    @Transaction
//    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
//    fun getDonorAndAllProducts(searchLast: String, searchFirst: String): Single<List<DonorWithProducts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalProducts(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalProduct(products: List<Product>)

    // Donors and Products

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullNameWithProducts(searchLast: String, searchFirst :String) : List<DonorWithProducts>

    @Query("SELECT * from donors")
    fun loadAllDonorsWithProducts(): List<DonorWithProducts>


}

data class DonorProduct(val donor: String?, val din: String?)