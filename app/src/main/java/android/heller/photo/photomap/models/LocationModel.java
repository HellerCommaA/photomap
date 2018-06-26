package android.heller.photo.photomap.models;

import android.content.Context;
import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.database.PhotoLocation;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;
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

    public static void setContext(final Context xContext) {
        getInstance().mContext = xContext;
        getInstance().mDb = AppDatabase.getDatabase(getInstance().mContext);
    }

    private LocationModel() {
        mMarkers = new HashMap<>();
    }

    public Marker addMarker(final UUID xUuid, final Marker xMarker) {
        mMarkers.put(xUuid, xMarker);
        PhotoLocation loc = new PhotoLocation();
        loc.id = xUuid.toString();
        loc.lat = xMarker.getPosition().latitude;
        loc.lon = xMarker.getPosition().longitude;
        loc.name = xMarker.getTitle();
        return xMarker;
    }

    public Marker addMarker(final Marker xMarker) {
        return addMarker(UUID.randomUUID(), xMarker);
    }

    public Marker getMarker(UUID xUuid) {
        return mMarkers.get(xUuid);
    }
    public Marker getMarker(String xUuid) {
        return mMarkers.get(UUID.fromString(xUuid));
    }

    public void removeMarkerFromList(final Marker xMarker) {
        if (xMarker == null) return;
        mDb.locationModel().delete(xMarker.getId());
        if (mMarkers.containsValue(xMarker)) {
            for(UUID k : mMarkers.keySet()) {
                if (mMarkers.get(k).equals(xMarker)) {
                    mMarkers.remove(k);
                    return;
                }
            }
        }
    }

    public UUID getUuidForMarker(final Marker xMarker) {
        UUID uuid = null;
        for(UUID k : mMarkers.keySet()) {
            if (mMarkers.get(k).equals(xMarker)) {
                uuid = k;
                break;
            }
        }
        // TODO check that the value returned from the database and the value found in mMap are equal
        return uuid;
    }

    public void removeMarkerFromMap(Marker xMarker) {
        xMarker.remove();
        mDb.locationModel().delete(xMarker.getId());
    }

    // load saved locations from memory
    public List<PhotoLocation> loadSavedLocations() {
        return mDb.locationModel().loadAllLocations();
    }
}
