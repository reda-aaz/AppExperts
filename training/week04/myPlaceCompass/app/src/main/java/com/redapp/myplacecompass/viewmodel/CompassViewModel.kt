package com.redapp.myplacecompass.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.redapp.myplacecompass.R
import com.redapp.myplacecompass.model.PlacesRepository
import com.redapp.myplacecompass.model.data.Result
import com.redapp.myplacecompass.util.GeofencingConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CompassViewModel(private val placesRepository: PlacesRepository): ViewModel() {

    val placesMutableData: MutableLiveData<List<Result>> = MutableLiveData()
//    val stateLiveData: MutableLiveData<List<Result>> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getGetNearbyPlaces( location: String, radius: Int, type: String) {
        compositeDisposable.clear()
        compositeDisposable.add(placesRepository.getPlacesNearby(
            location, radius, type
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ placesList ->
                placesMutableData.value = placesList
            }, { throwable ->
                Log.d("TAG_X", "${throwable.localizedMessage}")
            })
        )
    }



    private val _geofenceIndex = MutableLiveData<Int>()
    private val _monumentIndex = MutableLiveData<Int>()
    val geofenceIndex: LiveData<Int>
        get() = _geofenceIndex

    val geofenceMonumentResourceId = Transformations.map(geofenceIndex) {
        val index = geofenceIndex?.value ?: -1
        when {
            index < 0 -> R.string.not_started
            index < GeofencingConstants.NUM_LANDMARKS -> GeofencingConstants.LANDMARK_DATA[geofenceIndex.value!!].message
            else -> R.string.geofence_over
        }
    }
    init {
        _geofenceIndex.value = -1
        _monumentIndex.value = 0
    }

    fun updateMonument(currentIndex: Int) {
        _monumentIndex.value = currentIndex+1
    }

    fun geofenceActivated() {
        _geofenceIndex.value = _monumentIndex.value
    }

    fun geofenceIsActive() =_geofenceIndex.value == _monumentIndex.value
    fun nextGeofenceIndex() = _monumentIndex.value ?: 0
}


