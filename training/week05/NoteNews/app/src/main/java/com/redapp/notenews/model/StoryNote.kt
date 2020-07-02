package com.redapp.notenews.model

import android.text.format.DateUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stories")
data class StoryNote (@PrimaryKey(autoGenerate = true ) var storyId:Long = -1,
                 var title: String, var body: String, var date: Date = Date())

