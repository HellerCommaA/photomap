package android.heller.photo.photomap.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.heller.photo.photomap.models.LocationModel;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("select * from LocationData")
    List<LocationData> loadAllLocations();

    @Insert()
    void insertLocation(LocationData xModel);

    @Query("DELETE FROM LocationData")
    void deleteAll();
}
