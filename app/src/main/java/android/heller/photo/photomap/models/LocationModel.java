package android.heller.photo.photomap.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

public class LocationModel {

    private static LocationModel mInstance = null;
    private Map<LatLng, Marker> mMap = new HashMap<>();

    private LocationModel() {}

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













}
