package com.reda.mytraining.mylocation.viewmodel;

import android.text.BoringLayout;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reda.mytraining.mylocation.model.LocationRepository;
import com.reda.mytraining.mylocation.model.data.LocationDetails;
import com.reda.mytraining.mylocation.model.network.GeoCodeRetrofitInstance;
import com.reda.mytraining.mylocation.model.network.GeoCodeService;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LocationViewModel extends ViewModel {
    private static final String TAG = "LocationViewModel";
    private LocationRepository locationRepository = new LocationRepository();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<LocationDetails> mutableLiveDataLocation = new MutableLiveData<>();

    public LiveData<LocationDetails> getLocationLiveData() {
        return mutableLiveDataLocation;
    }

    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    public LiveData<Boolean> getShowProgress() {
        return showProgress;
    }

    public LocationViewModel() {
        showProgress.setValue(false);
    }

    public void searchLocationDetailsByAddress(String address) {
        showProgress.setValue(true);
        compositeDisposable.clear();
        compositeDisposable.add(locationRepository.getLocationDetailsByAddress(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                        locationDetails -> {mutableLiveDataLocation.setValue(locationDetails);
                            Log.i(TAG, "searchLocationDetailsByAddress: ");},
                        e -> {
                            showProgress.setValue(false);
                            Log.e(TAG, "getLocationDetailsByAddress: ", e);
                        }));
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
