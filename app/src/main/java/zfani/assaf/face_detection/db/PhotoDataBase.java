package zfani.assaf.face_detection.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import zfani.assaf.face_detection.interfaces.PhotoModelDao;
import zfani.assaf.face_detection.models.PhotoModel;

@Database(entities = {PhotoModel.class}, version = 1, exportSchema = false)
public abstract class PhotoDataBase extends RoomDatabase {

    private static PhotoDataBase INSTANCE;

    public static PhotoDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, PhotoDataBase.class, "photo.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public abstract PhotoModelDao getPhotoModelDAO();
}
