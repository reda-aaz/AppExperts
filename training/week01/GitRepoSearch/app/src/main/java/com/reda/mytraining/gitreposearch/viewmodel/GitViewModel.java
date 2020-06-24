package com.reda.mytraining.gitreposearch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reda.mytraining.gitreposearch.model.GitRepository;
import com.reda.mytraining.gitreposearch.model.data.Repository;

import java.util.List;

public class GitViewModel extends ViewModel {
    private GitRepository gitRepository = new GitRepository();

    private MutableLiveData<Boolean> isSearching = new MutableLiveData<>();
    public LiveData<Boolean> showProgressBar() {
        return isSearching;
    }

    public GitViewModel() {
        isSearching.setValue(false);
    }


    public LiveData<List<Repository>> getResults(String searchString) {
        isSearching.setValue(true);
        return gitRepository.getSearchResults(searchString);
    }

    public LiveData<List<Repository>> getRepos() {
        isSearching.setValue(true);
        return gitRepository.getRepositoryLiveData();
    }

    public void doneSearching() {
        isSearching.setValue(false);
    }


}