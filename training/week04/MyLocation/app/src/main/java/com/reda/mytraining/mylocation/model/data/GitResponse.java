package com.reda.mytraining.mylocation.model.data;

public class GitResponse {
    private LocationDetails locationDetails;
    private Throwable error;

    public GitResponse(LocationDetails locationDetails) {
        this.locationDetails = locationDetails;
        error = null;
    }

    public GitResponse(Throwable error) {
        locationDetails = null;
        this.error = error;
    }

    public LocationDetails getLocationDetails() {
        return locationDetails;
    }

    public Throwable getError() {
        return error;
    }



}
