package zfani.assaf.face_detection.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import zfani.assaf.face_detection.R;
import zfani.assaf.face_detection.workers.CleanupWorker;
import zfani.assaf.face_detection.workers.PhotoWorker;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> photoTabLiveData;
    private final MutableLiveData<Boolean> faceDetectionLiveData;
    private final LiveData<WorkInfo> photoWorkStatus;
    private String[] tabTitleList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        photoTabLiveData = new MutableLiveData<>();
        faceDetectionLiveData = new MutableLiveData<>();
        WorkManager workManager = WorkManager.getInstance();
        WorkContinuation workContinuation = workManager.beginWith(new OneTimeWorkRequest.Builder(CleanupWorker.class).build());
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PhotoWorker.class).build();
        workContinuation = workContinuation.then(oneTimeWorkRequest);
        workContinuation.enqueue();
        photoWorkStatus = workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.getId());
    }

    public String[] getTabTitleList() {
        if (tabTitleList == null) {
            tabTitleList = getApplication().getResources().getStringArray(R.array.tab_titles);
        }
        return tabTitleList;
    }

    public LiveData<WorkInfo> getPhotoWorkStatus() {
        return photoWorkStatus;
    }

    public LiveData<Integer> getPhotoTabLiveData() {
        return photoTabLiveData;
    }

    public void setPhotoTabLiveData(int photoTabLiveData) {
        this.photoTabLiveData.setValue(photoTabLiveData);
    }

    public LiveData<Boolean> getFaceDetectionLiveData() {
        return faceDetectionLiveData;
    }

    public void enableFaceDetection() {
        this.faceDetectionLiveData.setValue(true);
    }
}
