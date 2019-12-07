package com.fullsekurity.theatreblood.repository.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie


@Dao
interface DBDao {
    /**
     * Get the Movies from the table.
     * -------------------------------
     * Since the DB use as caching, we don't return LiveData.
     * We don't need to get update every time the database update.
     * We using the get query when application start. So, we able to display
     * data fast and in case we don't have connection to work offline.
     * @return the moviesObservable from the table
     */
    @get:Query("SELECT * FROM moviesObservable")
    val movies: List<Movie>

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: Movie)

    @Query("DELETE FROM moviesObservable")
    fun deleteAllMovies()
}