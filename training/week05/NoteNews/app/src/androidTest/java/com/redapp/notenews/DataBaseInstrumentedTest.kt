//package com.redapp.notenews
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.redapp.notenews.model.StoryNote
//import com.redapp.notenews.model.db.StoriesDataBase
//import com.redapp.notenews.model.db.StoryDAO
//import org.junit.After
//
//import org.junit.Test
//import org.junit.runner.RunWith
//
//import org.junit.Assert.*
//import org.junit.Before
//import java.util.*
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@RunWith(AndroidJUnit4::class)
//class DataBaseInstrumentedTest {
//    private lateinit var dbTest: StoriesDataBase
//    private lateinit var storyDAO: StoryDAO
//    @Before
//    fun createDataBase(){
//        val context = InstrumentationRegistry.getInstrumentation().context
//        dbTest = Room.inMemoryDatabaseBuilder(context,StoriesDataBase::class.java).allowMainThreadQueries().build()
//        storyDAO = dbTest.storyDAO
//    }
//
//    @After
//    fun closeDb()   {
//        lazy {
//        dbTest.close()
//        }
//    }
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val storyNote = StoryNote(title = "My tittle", body = "The story")
//        storyDAO.upsertStoryNote(storyNote)
//        val storiesLiveData = storyDAO.getStoriesNotes()
//        assert(storiesLiveData.value?.isNotEmpty()?: false)
//
//    }
//}