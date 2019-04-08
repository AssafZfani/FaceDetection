package zfani.assaf.face_detection.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileFilter;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import zfani.assaf.face_detection.db.PhotoDataBase;
import zfani.assaf.face_detection.models.PhotoModel;
import zfani.assaf.face_detection.utilities.BitmapHelper;
import zfani.assaf.face_detection.utilities.Constants;

public class PhotoWorker extends Worker {

    public PhotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
        String pathName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/";
        //String pathName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images/";
        File[] files = new File(pathName).listFiles(new ImageFileFilter());
        for (File file : files) {
            Bitmap bitmap = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(), 450, 450);
            String outputPhotoPath = BitmapHelper.writeBitmapToFile(getApplicationContext(), bitmap);
            boolean hasFace = faceDetector.detect(new Frame.Builder().setBitmap(bitmap).build()).size() > 0;
            PhotoDataBase.getDatabase(getApplicationContext()).getPhotoModelDAO().addPhoto(new PhotoModel(outputPhotoPath, hasFace));
        }
        return Result.success(new Data.Builder().putInt(Constants.KEY_PHOTO_SUM, files.length).build());
    }

    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return isImageFile(file.getAbsolutePath());
        }

        // Checks the file to see if it has a compatible extension.
        private boolean isImageFile(@NonNull String filePath) {
            return filePath.endsWith(".jpg") || filePath.endsWith(".png");
        }
    }
}
