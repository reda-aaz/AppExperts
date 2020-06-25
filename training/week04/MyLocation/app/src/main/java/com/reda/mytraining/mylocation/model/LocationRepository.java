package com.reda.mytraining.mylocation.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.reda.mytraining.mylocation.model.data.GitResponse;
import com.reda.mytraining.mylocation.model.data.LocationDetails;
import com.reda.mytraining.mylocation.model.network.GeoCodeRetrofitInstance;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class LocationRepository {

    private static final String TAG = "LocationRepository";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GeoCodeRetrofitInstance geoCodeRetrofitInstance;

    private MutableLiveData<GitResponse> gitResponseLiveData = new MutableLiveData<>();

    public LocationRepository() {
        geoCodeRetrofitInstance = GeoCodeRetrofitInstance.getInstance();
    }

    public LiveData<GitResponse> getLocationDetailsByAddress(String address) {
        compositeDisposable.clear();
        compositeDisposable.add(geoCodeRetrofitInstance.getLocationDetailsByAddress(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(

                        locationDetails -> gitResponseLiveData.setValue(new GitResponse(locationDetails)),
                        e -> {
                            gitResponseLiveData.setValue(new GitResponse(e));
                            Log.e(TAG, "getLocationDetailsByAddress: ", e);
                        }));
        return gitResponseLiveData;
    }


}