package com.reda.mytraining.mylocation.model.network;

import com.reda.mytraining.mylocation.model.data.LocationDetails;
import com.reda.mytraining.mylocation.model.data.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.reda.mytraining.mylocation.util.Constants.ADDRESS_PARAM;
import static com.reda.mytraining.mylocation.util.Constants.GEOCODE_ENDPOINT;
import static com.reda.mytraining.mylocation.util.Constants.KEY_PARAM;

public interface GeoCodeService {
    @GET(GEOCODE_ENDPOINT)
    Observable<LocationDetails> getLocationDetailsByAddress(@Query(ADDRESS_PARAM) String address,
                                                                  @Query(KEY_PARAM) String key);

}
