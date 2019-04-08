package zfani.assaf.face_detection;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class App extends Application implements LifecycleObserver {

    public static boolean isAppInForeground;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onMoveToForeground() {
        // app moved to foreground
        isAppInForeground = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onMoveToBackground() {
        // app moved to background
        isAppInForeground = false;
    }
}
