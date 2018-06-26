package android.heller.photo.photomap.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PhotoLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    public abstract LocationDao locationModel();

    public static AppDatabase getDatabase(Context xContext) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(xContext.getApplicationContext(), AppDatabase.class, "heller.photo")
                    .allowMainThreadQueries()
                    .build();
        }
        return mInstance;
    }

    public static void destroy() {
        mInstance = null;
    }

}
