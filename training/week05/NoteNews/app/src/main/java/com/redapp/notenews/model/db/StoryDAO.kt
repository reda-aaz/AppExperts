package com.redapp.notenews.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.redapp.notenews.model.StoryNote

@Dao
interface StoryDAO {

    @Query("SELECT * FROM stories")
    fun getStoriesNotes():LiveData<List<StoryNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertStoryNote(storyNote: StoryNote)

    @Delete
    fun deleteStoryNote(storyNote: StoryNote)

    @Query("DELETE FROM stories")
    fun deleteAllStories()
}