package zfani.assaf.face_detection.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.face_detection.R;
import zfani.assaf.face_detection.adapters.PhotoPagerAdapter;
import zfani.assaf.face_detection.utilities.AlertHelper;
import zfani.assaf.face_detection.utilities.Constants;
import zfani.assaf.face_detection.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vpPhotoPager)
    ViewPager vpPhotoPager;
    @BindView(R.id.tlPhotoTab)
    TabLayout tlPhotoTab;
    @BindView(R.id.btnDetectFaces)
    View btnDetectFaces;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.KEY_ACTION_APPLICATION_DETAILS_SETTINGS:
                checkStoragePermission();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.KEY_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initPhotoTabs();
                } else {
                    AlertHelper.showPermissionRequestAlert(this);
                }
                break;
        }
    }

    @OnClick(R.id.btnDetectFaces)
    void detectFaces() {
        FaceDetector faceDetector = new FaceDetector.Builder(MainActivity.this).setTrackingEnabled(false).build();
        if (faceDetector.isOperational()) {
            mainViewModel.enableFaceDetection();
        } else {
            AlertHelper.makeAlert(this, getString(R.string.face_detector_setup_failed_message));
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.KEY_REQUEST_STORAGE);
            return;
        }
        initPhotoTabs();
    }

    private void initPhotoTabs() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        vpPhotoPager.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager(), mainViewModel.getTabTitleList()));
        tlPhotoTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewModel.setPhotoTabLiveData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tlPhotoTab.setupWithViewPager(vpPhotoPager);
        mainViewModel.getFaceDetectionLiveData().observe(this, hasFaceDetectionEnabled -> btnDetectFaces.setVisibility(hasFaceDetectionEnabled ? View.GONE : View.VISIBLE));
        mainViewModel.getPhotoWorkStatus().observeForever(workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                AlertHelper.sendAlertOrNotification(this, getString(R.string.photos_categorizing_completed, workInfo.getOutputData().getInt(Constants.KEY_PHOTO_SUM, 0)));
            }
        });
    }
}
