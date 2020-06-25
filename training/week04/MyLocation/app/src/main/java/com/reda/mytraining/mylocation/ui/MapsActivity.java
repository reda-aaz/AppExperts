package com.reda.mytraining.mylocation.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reda.mytraining.mylocation.R;
import com.reda.mytraining.mylocation.model.data.GitResponse;
import com.reda.mytraining.mylocation.model.data.Location;
import com.reda.mytraining.mylocation.model.data.LocationDetails;
import com.reda.mytraining.mylocation.viewmodel.LocationViewModel;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationViewModel locationViewModel;
    private Button searchButton;
    private EditText addressEditText;
    private ProgressBar progressBar;
    private TextView latLngTextView;
    private LocationManager locationManager;

    private Observer<Boolean> showProgressObserver = showProgress -> {
        if (showProgress)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    };

    private Observer<GitResponse> locationDetailsObserver = gitResponse -> {
        if (gitResponse.getError() == null) {

            LocationDetails locationDetails = gitResponse.getLocationDetails();

            if (locationDetails != null && locationDetails.getResults().size() > 0) {

                Location location = locationDetails.getResults().get(0).getGeometry().getLocation();

                LatLng currentLocation = new LatLng(location.getLat(), location.getLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                mMap.clear();

                String latLngString = location.getLat() + " : " + location.getLng();
                mMap.addMarker(new MarkerOptions().position(currentLocation).title(latLngString));
                latLngTextView.setText(latLngString);

            } else {
                Log.i(TAG, "no results found");
                Toast.makeText(MapsActivity.this, getString(R.string.noResultsFound), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(MapsActivity.this, getString(R.string.error_occurred), Toast.LENGTH_LONG).show();

        }
        locationViewModel.doneSearching();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        addressEditText = findViewById(R.id.address_editText);
        progressBar = findViewById(R.id.progressBar);
        searchButton = findViewById(R.id.search_button);
        latLngTextView = findViewById(R.id.latlng_textView);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);


        searchButton.setOnClickListener(v -> {
            String address = addressEditText.getText().toString().trim();
            Log.d(TAG, "onCreate: " + address);
            if (!address.isEmpty()) {
                locationViewModel.searchLocationDetailsByAddress(address);
            }
        });

        locationViewModel.getShowProgress().observe(this, showProgressObserver);

        locationViewModel.liveDataResponse.observe(this, locationDetailsObserver);
    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }




}