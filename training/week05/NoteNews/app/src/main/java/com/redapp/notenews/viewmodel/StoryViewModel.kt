package com.redapp.notenews.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.redapp.notenews.model.StoryNote
import com.redapp.notenews.model.StoryRepository
import kotlinx.coroutines.*

class StoryViewModel(private val storyRepository: StoryRepository):ViewModel() {
    val storiesNotesLiveData: LiveData<List<StoryNote>> = storyRepository.getStoriesNotes()
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun upsertStoryNote(storyNote: StoryNote){
        uiScope.launch {
            upsert(storyNote)
        }
    }

    private suspend fun upsert(storyNote: StoryNote) {
        withContext(Dispatchers.IO) {
            storyRepository.upsertStoryNotes(storyNote)
        }
    }

    fun deleteStoryNote(storyNote: StoryNote){
        uiScope.launch {
            delete(storyNote)
        }
    }

    private suspend fun delete(storyNote: StoryNote) {
        withContext(Dispatchers.IO) {
            storyRepository.deleteStoryNote(storyNote)
        }
    }

    fun deleteAllStoriesNotes(){
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            storyRepository.deleteAllStoriesNotes()
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}