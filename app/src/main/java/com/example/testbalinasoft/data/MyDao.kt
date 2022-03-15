package com.example.testbalinasoft.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDao {

    @Query("DELETE FROM image_table")
    suspend fun clearImages()

    @Query("SELECT * FROM image_table")
    fun getImages(): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

    @Query("SELECT * FROM comment_table WHERE imageId = :imageId")
    fun getComments(imageId: Int): Flow<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)
}