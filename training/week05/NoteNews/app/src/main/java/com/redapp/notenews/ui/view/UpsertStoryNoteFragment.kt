package com.redapp.notenews.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redapp.notenews.R


private const val ARG_PARAM1 = "storyId"

class UpsertStoryNoteFragment : Fragment() {
    private var storyId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storyId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upsert_story_note, container, false)
    }

    companion object {
        fun newInstance(param1: Int?) =
            UpsertStoryNoteFragment().apply {
                param1?.let {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                    }
                }
            }
    }
}