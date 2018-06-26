package android.heller.photo.photomap.models;

import android.content.Context;
import android.content.res.Resources;
import android.heller.photo.photomap.database.AppDatabase;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocationModel {
    private static LocationModel mInstance;
    private Map<UUID, Marker> mMarkers;
    private final String TAG = LocationModel.class.getSimpleName();
    private AppDatabase mDb;
    private Context mContext;
    public static LocationModel getInstance() {
        if (mInstance == null) {
            mInstance = new LocationModel();
        }
        return mInstance;
    }

    public static void setContext(Context xContext) {
        getInstance().mContext = xContext;
    }

    private LocationModel() {
        mMarkers = new HashMap<>();
        mDb = AppDatabase.getDatabase(mContext);
    }

    public Marker addMarker(Marker xMarker) {
        UUID uuid = UUID.randomUUID();
        mMarkers.put(uuid, xMarker);
        return xMarker;
    }

    public Marker getMarker(UUID xUuid) {
        return mMarkers.get(xUuid);
    }

    public void removeMarkerFromList(Marker xMarker) {
        if (xMarker == null) return;
        if (mMarkers.containsValue(xMarker)) {
            for(UUID k : mMarkers.keySet()) {
                if (mMarkers.get(k).equals(xMarker)) {
                    mMarkers.remove(k);
                    return;
                }
            }
        }
    }

    public UUID getUuidForMarker(Marker xMarker) {
        for(UUID k : mMarkers.keySet()) {
            if (mMarkers.get(k).equals(xMarker)) {
                Log.d(TAG, "getUuidForMarker: FOUND MATCH");
                return k;
            }
            Log.d(TAG, "getUuidForMarker: DID NOT FIND MATCH");
        }
        return null;
    }

    public void removeMarkerFromMap(Marker xMarker) {
        xMarker.remove();
    }

}
