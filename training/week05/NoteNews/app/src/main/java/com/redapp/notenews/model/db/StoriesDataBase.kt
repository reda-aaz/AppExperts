package com.redapp.notenews.model.db

import android.content.Context
import androidx.room.*
import com.redapp.notenews.model.StoryNote

@Database(entities = [StoryNote::class],version = 1 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class StoriesDataBase: RoomDatabase() {
    abstract val storyDAO: StoryDAO

    companion object{
        @Volatile
        private var INSTANCE: StoriesDataBase? = null
        fun getInstance(context: Context):StoriesDataBase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, StoriesDataBase::class.java,"stories_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

    }

}