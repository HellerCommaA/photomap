package android.heller.photo.photomap.database.utils;

import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.database.LocationData;
import android.support.annotation.NonNull;

public class DatabaseInit {

    public static void populateSync(@NonNull final AppDatabase xDb) {
//        xDb.locationDatabase().deleteAll();
        addLocation(xDb, "test", 1234, 1234, "test_name");
    }

    private static void addLocation(final AppDatabase xDb, String xId, long xLat, long xLon, String xName) {
        LocationData data = new LocationData();
        data.id = xId;
        data.lat = xLat;
        data.lon = xLon;
        data.name = xName;
        xDb.locationDatabase().insertLocation(data);
    }
}
