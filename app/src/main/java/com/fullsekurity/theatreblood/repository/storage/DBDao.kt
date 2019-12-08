package com.fullsekurity.theatreblood.repository.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor


@Dao
interface DBDao {
    @get:Query("SELECT * FROM moviesObservable")
    val donors: List<Donor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDonor(donor: Donor)

    @Query("DELETE FROM moviesObservable")
    fun deleteAllDonors()
}