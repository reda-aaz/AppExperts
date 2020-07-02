package com.redapp.notenews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redapp.notenews.model.StoryRepository
import java.lang.IllegalArgumentException

class StoryViewModelFactory(private val storyRepository: StoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}