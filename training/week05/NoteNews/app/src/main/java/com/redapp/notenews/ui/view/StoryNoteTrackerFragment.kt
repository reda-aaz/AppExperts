package com.redapp.notenews.ui.view

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redapp.notenews.R
import com.redapp.notenews.databinding.FragmentStoryNoteTrackerBinding
import com.redapp.notenews.model.StoryNote
import com.redapp.notenews.model.StoryRepository
import com.redapp.notenews.ui.adapter.CustomItemTouchHelper
import com.redapp.notenews.ui.adapter.StoryAdpater
import com.redapp.notenews.viewmodel.StoryViewModel
import com.redapp.notenews.viewmodel.StoryViewModelFactory



class StoryNoteTrackerFragment : Fragment() {

    private lateinit var repository: StoryRepository
    private lateinit var viewModel: StoryViewModel
    private lateinit var callback: OnUpsertNoteListner

    fun setOnUpsertListner(callback: OnUpsertNoteListner){
        this.callback = callback
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        repository = StoryRepository(requireActivity().application)
        val viewModelFactory = StoryViewModelFactory(repository)
        val binding: FragmentStoryNoteTrackerBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_story_note_tracker,container,false)
        val adapter = StoryAdpater()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())



        val callback: ItemTouchHelper.SimpleCallback = CustomItemTouchHelper(adapter, object :CustomItemTouchHelper.CustomSwipListner{
            override fun onSwipedNote(storyNote: StoryNote) {
                viewModel.deleteStoryNote(storyNote)
            }
        })
        val itemTouchHelper:  ItemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.lifecycleOwner =this

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StoryViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.storiesNotesLiveData.observe(this, Observer { list -> adapter.submitList(list) })
        viewModel.upsertStoryNote(StoryNote(title = "new York", body = "This story deals with ..."))
        binding.floatingAddingButton.setOnClickListener {
            this.callback.onUpsertNote(null)
        }
        return binding.root


    }

    interface OnUpsertNoteListner{
        fun onUpsertNote(storyNoteId: Int?)
    }

}