package zfani.assaf.face_detection.workers;

import android.content.Context;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import zfani.assaf.face_detection.db.PhotoDataBase;
import zfani.assaf.face_detection.utilities.Constants;

public class CleanupWorker extends Worker {

    public CleanupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        boolean deleted = true;
        File outputDirectory = new File(getApplicationContext().getFilesDir(), Constants.KEY_WORK_OUTPUT_PATH);
        if (outputDirectory.exists()) {
            File[] entries = outputDirectory.listFiles();
            if (entries != null && entries.length > 0) {
                for (File entry : entries) {
                    String name = entry.getName();
                    if (!name.isEmpty() && (name.endsWith(".png") || name.endsWith(".jpg"))) {
                        deleted = entry.delete();
                    }
                }
            }
        }
        PhotoDataBase.getDatabase(getApplicationContext()).getPhotoModelDAO().deleteAllPhotos();
        return deleted ? Result.success() : Result.failure();
    }
}
