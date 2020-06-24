package com.reda.mytraining.gitreposearch.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.reda.mytraining.gitreposearch.model.data.Repository;
import com.reda.mytraining.gitreposearch.model.network.GitRetrofitInstance;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GitRepository {
    private GitRetrofitInstance retrofitInstance;

    public GitRepository() {
        retrofitInstance = GitRetrofitInstance.getRetrofitInstance();
    }


    public Observable<List<Repository>> getSearchResults(String userName) {
        return retrofitInstance.getUserGitRepository(userName);
    }


}
