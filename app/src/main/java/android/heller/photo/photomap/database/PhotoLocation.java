package android.heller.photo.photomap.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity()
public class PhotoLocation {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public double lat;
    public double lon;
}
