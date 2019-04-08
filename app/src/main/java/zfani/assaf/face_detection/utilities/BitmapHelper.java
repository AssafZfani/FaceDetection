package zfani.assaf.face_detection.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

public class BitmapHelper {

    public static Bitmap decodeBitmapFromFile(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateSampleSize(options, height, width);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        Matrix matrix = fixOrientationImageByPath(imagePath);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Matrix fixOrientationImageByPath(String pathName) {
        try {
            ExifInterface exif = new ExifInterface(pathName);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }
            return matrix;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static String writeBitmapToFile(@NonNull Context context, @NonNull Bitmap bitmap) {
        String name = String.format("output-%s.png", UUID.randomUUID().toString());
        File outputDir = new File(context.getFilesDir(), Constants.KEY_WORK_OUTPUT_PATH);
        boolean fileExistsOrCreated = true;
        if (!outputDir.exists()) {
            fileExistsOrCreated = outputDir.mkdir();
        }
        if (!fileExistsOrCreated) {
            return "";
        }
        File outputFile = new File(outputDir, name);
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(outputFile).toString();
    }
}
