package android.heller.photo.photomap.models;

import android.content.Context;
import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.database.PhotoLocation;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LocationModel {
    private static LocationModel mInstance;
    private List<Marker> mMarkers;
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
        mMarkers = new LinkedList<>();
    }

    private Marker addCompletedMarker(final Marker xMarker) {
        mMarkers.add(xMarker);
        PhotoLocation loc = new PhotoLocation();
        loc.id = Objects.requireNonNull(xMarker.getTag().toString());
        loc.lat = xMarker.getPosition().latitude;
        loc.lon = xMarker.getPosition().longitude;
        loc.name = xMarker.getTitle();
        mDb.locationModel().insertLocation(loc);
        return xMarker;
    }

    public Marker addMarker(final Marker xMarker) {
        if (xMarker.getTag() != null) {
            return addCompletedMarker(xMarker);
        } else {
            UUID uuid = UUID.randomUUID();
            xMarker.setTag(uuid.toString());
            return addMarker(xMarker);
        }
    }

    public Marker getMarker(String xUuid) {
        for (Marker m : mMarkers) {
            if (m.getTag() != null) {
                if (m.getTag().toString().equals(xUuid)) {
                    return m;
                }
            }
        }
        return null;
    }

    public void removeMarkerFromMap(Marker xMarker) {
        String uuid = (String) xMarker.getTag();
        mDb.locationModel().delete(uuid);
        mMarkers.remove(xMarker);
        xMarker.setVisible(false);
        xMarker.remove();


    }

    // load saved locations from memory
    public List<PhotoLocation> loadSavedLocations(GoogleMap xMap) {
        List<PhotoLocation> list = mDb.locationModel().loadAllLocations();
        for (PhotoLocation each : list) {
            MarkerOptions m = new MarkerOptions();
            m.position(new LatLng(each.lat, each.lon));
            m.title(each.name);
            Marker marker = xMap.addMarker(m);
            marker.setTag(each.id);
            mMarkers.add(marker);
        }
        return list;
    }
}
