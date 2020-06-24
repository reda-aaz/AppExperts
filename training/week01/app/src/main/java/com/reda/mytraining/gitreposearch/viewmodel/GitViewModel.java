package com.reda.mytraining.gitreposearch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.reda.mytraining.gitreposearch.model.GitRepository;
import com.reda.mytraining.gitreposearch.model.data.Repository;

import java.util.List;

public class GitViewModel extends ViewModel {
    private GitRepository gitRepository = new GitRepository();

    public LiveData<List<Repository>> getResults(String searchString) {
        return gitRepository.getSearchResults(searchString);
    }

    public LiveData<List<Repository>> getRepos() {
        return gitRepository.getRepositoryLiveData();
    }


}