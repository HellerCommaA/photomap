package android.heller.photo.photomap.models;

import android.content.Context;
import android.heller.photo.photomap.activities.PhotoMap;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocationModel {

    private final String TAG = LocationModel.class.getSimpleName();

    private static LocationModel mInstance = null;
    private Map<LatLng, Marker> mMap = new HashMap<>();
    private Context mContext;

    private LocationModel() {
    }

    public static LocationModel getInstance() {
        if (mInstance == null) {
            mInstance = new LocationModel();
        }
        return mInstance;
    }

    public Marker getMarker(LatLng xKey) {
        if (mMap.containsKey(xKey)) {
            return mMap.get(xKey);
        } else {
            return null;
        }
    }

    public Marker addLocation(LatLng xLat, Marker xMarker) {
        mMap.put(xLat, xMarker);
        return xMarker;
    }

    // called before activity destroyed
    public void save() {

    }

    // load from DB
    public void load(GoogleMap xMap) {

    }
}
