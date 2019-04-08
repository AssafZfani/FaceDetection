package zfani.assaf.face_detection.utilities;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import zfani.assaf.face_detection.App;
import zfani.assaf.face_detection.R;

public class AlertHelper {

    public static void sendAlertOrNotification(Context context, String message) {
        if (App.isAppInForeground) {
            makeAlert(context, message);
        } else {
            makeNotification(context, message);
        }
    }

    public static void makeAlert(Context context, String message) {
        new AlertDialog.Builder(context).setTitle("").setMessage(message).create().show();
    }

    private static void makeNotification(Context context, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);
        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build());
    }

    public static void showPermissionRequestAlert(Activity activity) {
        new AlertDialog.Builder(activity).setCancelable(false)
                .setTitle(activity.getString(R.string.dialog_storage_permission_title))
                .setMessage(activity.getString(R.string.dialog_storage_permission_message))
                .setPositiveButton(activity.getString(R.string.dialog_storage_permission_confirm), (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, Constants.KEY_ACTION_APPLICATION_DETAILS_SETTINGS);
                }).create().show();
    }
}
