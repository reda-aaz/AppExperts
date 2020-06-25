package com.reda.mytraining.mylocation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reda.mytraining.mylocation.model.LocationRepository;
import com.reda.mytraining.mylocation.model.data.GitResponse;

import io.reactivex.disposables.CompositeDisposable;

import static com.reda.mytraining.mylocation.util.Constants.FIRST_SEARCH;

public class LocationViewModel extends ViewModel {
    private static final String TAG = "LocationViewModel";
    private LocationRepository locationRepository = new LocationRepository();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<GitResponse> liveDataResponse = locationRepository.getLocationDetailsByAddress(FIRST_SEARCH); ;


    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    public LiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public LocationViewModel() {
        showProgress.setValue(false);
    }

    public void searchLocationDetailsByAddress(String address) {
        showProgress.setValue(true);
        liveDataResponse = locationRepository.getLocationDetailsByAddress(address);
    }
    public void doneSearching(){
        showProgress.setValue(false);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
