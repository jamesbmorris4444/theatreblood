package com.fullsekurity.theatreblood.repository

import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.repository.storage.Donor
import javax.inject.Inject

class Repository @Inject constructor(private val bloodDatabase: BloodDatabase) {

    fun insertIntoDatabase(donor: Donor) {
        bloodDatabase?.donorDao()?.insertDonor(donor)
    }

    fun getAllDonors(): List<Donor> {
        return bloodDatabase?.donorDao()?.donors
    }

}