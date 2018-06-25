package android.heller.photo.photomap.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {LocationData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    public abstract LocationDao locationDatabase();

    public static AppDatabase getInMemoryDb(Context xContext) {
        if (mInstance == null) {
            mInstance = Room.inMemoryDatabaseBuilder(xContext.getApplicationContext(), AppDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return mInstance;
    }

    public static void destroy() {
        mInstance = null;
    }

}
