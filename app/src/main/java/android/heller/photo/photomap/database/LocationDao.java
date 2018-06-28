package android.heller.photo.photomap.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * from PhotoLocation")
    List<PhotoLocation> loadAllLocations();

    @Insert()
    void insertLocation(PhotoLocation xModel);

    @Query("DELETE FROM PhotoLocation")
    void deleteAll();

    @Query("DELETE from PhotoLocation WHERE id = :xId")
    void delete(String xId);

    @Query("SELECT * from PhotoLocation WHERE id = :xId")
    PhotoLocation getLocation(String xId);
}
