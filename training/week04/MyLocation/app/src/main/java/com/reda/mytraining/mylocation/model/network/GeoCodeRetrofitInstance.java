package com.reda.mytraining.mylocation.model.network;

import com.reda.mytraining.mylocation.model.data.LocationDetails;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.reda.mytraining.mylocation.util.Constants.API_KEY;
import static com.reda.mytraining.mylocation.util.Constants.BASE_URL;

public class GeoCodeRetrofitInstance {
    private static GeoCodeService geoCodeService;
    private static GeoCodeRetrofitInstance retrofitInstance;

    private GeoCodeRetrofitInstance() {
        geoCodeService = getGeoCodeService();
    }

    private static GeoCodeService getGeoCodeService(){
        return geoCodeService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeoCodeService.class);
    }

    public static GeoCodeRetrofitInstance getInstance(){
        if(retrofitInstance == null){
            retrofitInstance = new GeoCodeRetrofitInstance();
        }
        return retrofitInstance;
    }

    public Observable<LocationDetails> getLocationDetailsByAddress(String address){
        return geoCodeService.getLocationDetailsByAddress(address,API_KEY);
    }

}
