package zfani.assaf.face_detection.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import zfani.assaf.face_detection.db.PhotoDataBase;
import zfani.assaf.face_detection.interfaces.PhotoModelDao;
import zfani.assaf.face_detection.models.PhotoModel;

public class PhotoViewModel extends AndroidViewModel {

    private final PhotoModelDao photoModelDao;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoModelDao = PhotoDataBase.getDatabase(getApplication()).getPhotoModelDAO();
    }

    public LiveData<List<PhotoModel>> getPhotoList(int photoTab) {
        switch (photoTab) {
            case 1:
                return photoModelDao.getFacesPhotos();
            case 2:
                return photoModelDao.getNonFacesPhotos();
            default:
                return photoModelDao.getAllPhotos();
        }
    }
}
