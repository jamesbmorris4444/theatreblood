package com.fullsekurity.theatreblood.repository.storage

import androidx.room.*
import io.reactivex.Single


@Dao
interface DBDao {

    // insertions

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDonor(donor: Donor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalDonors(donors: List<Donor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>)

    @Transaction
    fun insertDonorAndProducts(donor: Donor, products: List<Product>) {
        insertDonor(donor)
        insertProducts(products)
    }

    // queries

    @Query("SELECT COUNT(id) FROM donors")
    fun getDonorEntryCount(): Single<Int>

    @Query("SELECT COUNT(id) FROM products")
    fun getProductEntryCount(): Single<Int>

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullName(searchLast: String, searchFirst :String) : Single<List<Donor>>

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullNameWithProducts(searchLast: String, searchFirst :String) : Single<List<DonorWithProducts>>

    @Query("SELECT * FROM donors WHERE title = :searchLast AND poster_path = :searchFirst AND vote_count = :searchMiddle AND release_date = :searchDate")
    fun donorsFromNameAndDateWithProducts(searchLast: String, searchFirst :String, searchMiddle: String, searchDate :String) : Single<DonorWithProducts>

    // get all donors and products

    @Query("SELECT * from donors")
    fun loadAllDonorsWithProducts(): List<DonorWithProducts>

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM donors")
    fun getAllDonors(): List<Donor>


}