package android.heller.photo.photomap.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class LocationData {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public Long lat;
    public Long lon;
}
