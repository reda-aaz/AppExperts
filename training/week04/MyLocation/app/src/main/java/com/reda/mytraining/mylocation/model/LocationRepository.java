package com.reda.mytraining.mylocation.model;

import com.reda.mytraining.mylocation.model.data.LocationDetails;
import com.reda.mytraining.mylocation.model.network.GeoCodeRetrofitInstance;

import java.util.List;

import io.reactivex.Observable;

public class LocationRepository {
    private GeoCodeRetrofitInstance geoCodeRetrofitInstance;
    public LocationRepository() {
        geoCodeRetrofitInstance = GeoCodeRetrofitInstance.getInstance();
    }
    public Observable<LocationDetails> getLocationDetailsByAddress(String address){
        return geoCodeRetrofitInstance.getLocationDetailsByAddress(address);
    }


}