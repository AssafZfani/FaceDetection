package zfani.assaf.face_detection.interfaces;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import zfani.assaf.face_detection.models.PhotoModel;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PhotoModelDao {

    @Query("Select * from photo_table")
    LiveData<List<PhotoModel>> getAllPhotos();

    @Query("Select * from photo_table where hasFace = 1")
    LiveData<List<PhotoModel>> getFacesPhotos();

    @Query("Select * from photo_table where hasFace = 0")
    LiveData<List<PhotoModel>> getNonFacesPhotos();

    @Insert(onConflict = REPLACE)
    void addPhoto(PhotoModel photoModel);

    @Query("Delete from photo_table")
    void deleteAllPhotos();
}
