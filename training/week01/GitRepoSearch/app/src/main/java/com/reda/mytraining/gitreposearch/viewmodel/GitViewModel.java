package com.reda.mytraining.gitreposearch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reda.mytraining.gitreposearch.model.GitRepository;
import com.reda.mytraining.gitreposearch.model.data.Repository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GitViewModel extends ViewModel {
    private GitRepository gitRepository = new GitRepository();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<List<Repository>> repositoryLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSearching = new MutableLiveData<>();
    
    public LiveData<Boolean> showProgressBar() {
        return isSearching;
    }

    public GitViewModel() {
        isSearching.setValue(false);
    }


    public void getResults(String searchString) {
        isSearching.setValue(true);

        compositeDisposable.add(
                gitRepository.getSearchResults(searchString)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(repoList -> {
                            repositoryLiveData.setValue(repoList);

                        }, throwable -> {
                            Log.d("TAG_ERROR", "" + throwable.getLocalizedMessage());
                        })
        );
    }

    public LiveData<List<Repository>> getReposLiveData(){
        return repositoryLiveData;
    }

    public void doneSearching() {
        isSearching.setValue(false);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }


}