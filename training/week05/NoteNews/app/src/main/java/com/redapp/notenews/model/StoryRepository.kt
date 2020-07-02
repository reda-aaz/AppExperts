package com.redapp.notenews.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.redapp.notenews.model.db.StoriesDataBase
import com.redapp.notenews.model.db.StoryDAO

class StoryRepository(context: Context) {
    private val storyDAO : StoryDAO= StoriesDataBase.getInstance(context).storyDAO

    fun getStoriesNotes():LiveData<List<StoryNote>>{
        return storyDAO.getStoriesNotes()
    }

    fun upsertStoryNotes(storyNote: StoryNote){
        storyDAO.upsertStoryNote(storyNote)
    }

    fun deleteStoryNote(storyNote: StoryNote){
        storyDAO.deleteStoryNote(storyNote)
    }

    fun deleteAllStoriesNotes(){
        storyDAO.deleteAllStories()
    }
}