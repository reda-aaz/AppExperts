package com.redapp.myplacecompass.ui.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.redapp.myplacecompass.R
import com.redapp.myplacecompass.ui.PlaceImagesFragment
import com.redapp.myplacecompass.ui.adapter.PlaceAdapter
import com.redapp.myplacecompass.util.PlaceLocationListener
import com.redapp.myplacecompass.viewmodel.CompassVMFactory
import com.redapp.myplacecompass.viewmodel.CompassViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.redapp.myplacecompass.BuildConfig
import com.redapp.myplacecompass.model.data.Result
import com.redapp.myplacecompass.ui.PlaceImagesFragment.Companion.PLACE_KEY
import com.redapp.myplacecompass.util.GeofenceBroadcastReceiver
import com.redapp.myplacecompass.util.GeofencingConstants
import com.redapp.myplacecompass.util.createChannel
import kotlinx.android.synthetic.main.activity_maps.*

import java.util.jar.Manifest

private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val TAG = "HomeActivity"
private const val LOCATION_PERMISSION_INDEX = 0
private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1


class HomeActivity : AppCompatActivity(), OnMapReadyCallback, PopupMenu.OnMenuItemClickListener,
    PlaceLocationListener.LocationDelegate, PlaceAdapter.PlaceClickListener {

    private lateinit var map: GoogleMap

    private lateinit var activityMapsLayout: View

    private lateinit var geofencingClient: GeofencingClient

    private val placeAdapter: PlaceAdapter = PlaceAdapter(mutableListOf(), this)

    private val placeImageFragment: PlaceImagesFragment = PlaceImagesFragment()

    // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private var placeLocationListener: PlaceLocationListener = PlaceLocationListener(this)
    private val compassViewModel: CompassViewModel by viewModels<CompassViewModel>(
        factoryProducer = { CompassVMFactory() })

    private var radius = 1100

    lateinit var placeObserver: Observer<List<Result>>

    private fun displayResults(resultList: List<Result>?) {
        Log.d("TAG_X", "${resultList?.size}")
        resultList?.let { results ->
            placeAdapter.placeList = resultList
            placeAdapter.notifyDataSetChanged()

            drawOnMap(results)
        }
    }

    private fun drawOnMap(results: List<Result>) {

        results.forEach { placeItem ->

            val latLng = LatLng(placeItem.geometry.location.lat, placeItem.geometry.location.lng)
            map.addMarker(
                MarkerOptions().position(latLng).title(placeItem.name).icon(
                    BitmapDescriptorFactory.defaultMarker(150f)
                )
            )
        }
    }

    val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        checkPermissionAndStartGeofencing()
        activityMapsLayout = findViewById(R.id.map)
        recyclerView.adapter = placeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        geofencingClient = LocationServices.getGeofencingClient(this)

        // Create channel for notifications
        createChannel(this)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if ((recyclerView.adapter as PlaceAdapter).itemCount > 0)
                        scrollToPosition((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
                }
            }
        })
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        map_menu_imageview.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.place_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

        my_location_imageview.setOnClickListener {
            setLocation(placeLocationListener.locationLatLng)
        }

        placeObserver = Observer<List<com.redapp.myplacecompass.model.data.Result>> { resultList ->
            displayResults(resultList)
        }
        compassViewModel.placesMutableData.observe(this, placeObserver)
        compassViewModel.geofenceMonumentResourceId.observe(
            this,
            Observer { ressourceId -> visit_textView.text = getString(ressourceId) })


        map_visit_imageView.setOnClickListener {

            if (visit_textView.visibility == View.GONE) {
                visit_textView.visibility = View.VISIBLE
            } else {
                visit_textView.visibility = View.GONE
            }

            Log.i(TAG, "map Visit Click listner: ${visit_textView.visibility.toString()}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationManager() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            200,
            5f,
            placeLocationListener
        )
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun checkPermissionAndStartGeofencing() {
        Log.i(TAG, "checkPermissionAndStartGeofencing: ")
        if (compassViewModel.geofenceIsActive()) return
        if (areLocationPermissionsApproved()) {
            checkDeviceLocationSettings()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        Log.i(TAG, "requestLocationPermissions: ")
        if (areLocationPermissionsApproved())
            return
        var permissionArray = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        var requestCode = when {
            runningQOrLater -> {
                permissionArray += android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        ActivityCompat.requestPermissions(this@HomeActivity, permissionArray, requestCode)
    }

    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        Log.i(TAG, "checkDeviceLocationSettings: ")
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    exception.startResolutionForResult(
                        this@HomeActivity,
                        REQUEST_TURN_DEVICE_LOCATION_ON
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    activityMapsLayout,
                    getString(R.string.location_required_error),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettings()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                setLocationManager()
                addGeofenceForMonuments()
            }
        }
    }

    private fun areLocationPermissionsApproved(): Boolean {
        Log.i(TAG, "areLocationPermissionsApproved: ")
        val foregroundApproved = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val backgroundApproved = if (runningQOrLater) {
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else true
        return foregroundApproved && backgroundApproved
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (
            grantResults.isEmpty() || grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED)
        ) {
            Snackbar.make(
                this.findViewById(R.id.map),
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            checkDeviceLocationSettings()
        }
    }

    fun scrollToPosition(position: Int) {
        placeAdapter.placeList[position].let {
            val latLng = LatLng(it.geometry.location.lat, it.geometry.location.lng)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val placeType = when (item.itemId) {
            R.id.zoo_item -> "zoo"
            R.id.hospital_item -> "hospital"
            R.id.laundry_item -> "laundry"
            R.id.school_item -> "school"
            R.id.park_item -> "park"
            R.id.police_item -> "police"
            R.id.cafe_item -> "cafe"
            R.id.gym_item -> "gym"
            else -> ""
        }
        compassViewModel.getGetNearbyPlaces(placeLocationListener.locationString, radius, placeType)
        return true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun setLocation(location: Location) {

        var icon = BitmapFactory.decodeResource(resources, R.drawable.me_icon)
        icon = Bitmap.createScaledBitmap(icon, 300, 300, false)
        val currentLocation = LatLng(location.latitude, location.longitude)
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.addMarker(
            MarkerOptions().position(currentLocation).title("This is you!").icon(
                BitmapDescriptorFactory.fromBitmap(icon)
            )
        )
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

        map.addCircle(
            CircleOptions().center(currentLocation).radius(radius.toDouble())
                .fillColor(resources.getColor(R.color.blue_alpha75, resources.newTheme()))
        )

    }

    override fun selectPlace(place: Result) {
        supportFragmentManager.beginTransaction()
            .add(R.id.place_frame, placeImageFragment.also {
                it.arguments = Bundle().also { bundle ->
                    bundle.putSerializable(
                        PLACE_KEY, place
                    )
                }
            })
            .addToBackStack(placeImageFragment.tag)
            .commit()
        supportFragmentManager.executePendingTransactions()
    }

    @SuppressLint("MissingPermission")
    private fun addGeofenceForMonuments() {
        if (compassViewModel.geofenceIsActive()) return
        val currentGeodenceIndex = compassViewModel.nextGeofenceIndex()
        if (currentGeodenceIndex >= GeofencingConstants.NUM_LANDMARKS) {
            removeGeofences()
            compassViewModel.geofenceActivated()
            return
        }
        val currentGeofenceData = GeofencingConstants.LANDMARK_DATA.get(currentGeodenceIndex)

        val geofence = Geofence.Builder()
            .setRequestId(currentGeofenceData.id)
            .setCircularRegion(
                currentGeofenceData.latLong.latitude,
                currentGeofenceData.latLong.longitude,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        Toast.makeText(
                            this@HomeActivity,
                            R.string.geofences_added,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.e("Add Geofence: ", geofence.requestId)
                        compassViewModel.geofenceActivated()
                    }
                    addOnFailureListener {
                        Toast.makeText(
                            this@HomeActivity,
                            R.string.geofences_not_added,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        if ((it.message != null)) {
                            Log.w(TAG, it.message)
                        }
                    }
                }
            }
        }
    }

    private fun removeGeofences() {
        if (!areLocationPermissionsApproved())
            return
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d(TAG, getString(R.string.geofences_removed))
                Toast.makeText(
                    applicationContext,
                    R.string.geofences_removed, Toast.LENGTH_LONG
                )
                    .show()
            }
            addOnFailureListener {
                Log.e(TAG, getString(R.string.geofences_not_removed))
            }
        }
    }

    /*
     *  When the user clicks on the notification, this method will be called, letting us know that
     *  the geofence has been triggered, and it's time to move to the next one in the treasure
     *  hunt.
     */
    override fun onNewIntent(intent: Intent?) {
        Log.i(TAG, "onNewIntent: ")
        super.onNewIntent(intent)
        val extras = intent?.extras
        if (extras != null) {
            if (extras.containsKey(GeofencingConstants.EXTRA_GEOFENCE_INDEX)) {
                compassViewModel.updateMonument(extras.getInt(GeofencingConstants.EXTRA_GEOFENCE_INDEX))
                checkPermissionAndStartGeofencing()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeGeofences()
    }


    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
            "HomeActivity.redapp.action.ACTION_GEOFENCE_EVENT"
    }

}
