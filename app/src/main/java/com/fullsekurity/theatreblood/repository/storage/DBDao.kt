package com.fullsekurity.theatreblood.repository.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DBDao {
    @get:Query("SELECT * FROM donors")
    val donors: List<Donor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDonor(donor: Donor)

    @Query("DELETE FROM donors")
    fun deleteAllDonors()

    @Query("SELECT * FROM donors WHERE title LIKE :searchLast AND poster_path LIKE :searchFirst")
    fun donorsFromFullName(searchLast: String, searchFirst :String) : List<Donor>
}