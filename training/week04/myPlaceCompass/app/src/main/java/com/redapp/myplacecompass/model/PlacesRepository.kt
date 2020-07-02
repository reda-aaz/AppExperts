package com.redapp.myplacecompass.model

import com.redapp.myplacecompass.model.data.PlacesResponse
import com.redapp.myplacecompass.model.data.Result
import com.redapp.myplacecompass.model.network.PlacesRetrofitInstance
import io.reactivex.Observable

object PlacesRepository {

    fun getPlacesNearby(userLocation: String, radius: Int, type: String) : Observable<List<Result>> {
        return PlacesRetrofitInstance.getPlaces(userLocation, radius, type).map {
            it.results
        }
    }

}