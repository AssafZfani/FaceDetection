package zfani.assaf.face_detection.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.face_detection.R;
import zfani.assaf.face_detection.adapters.PhotoAdapter;
import zfani.assaf.face_detection.viewmodels.MainViewModel;
import zfani.assaf.face_detection.viewmodels.PhotoViewModel;

public class PhotoFragment extends Fragment {

    @BindView(R.id.rvPhotoList)
    RecyclerView rvPhotoList;
    @BindView(R.id.tvEmptyListMessage)
    TextView tvEmptyListMessage;
    private PhotoViewModel photoViewModel;
    private PhotoAdapter photoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        ButterKnife.bind(this, view);
        rvPhotoList.setAdapter(photoAdapter = new PhotoAdapter());
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainViewModel mainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getPhotoTabLiveData().observe(getActivity(), selectedPhotoTab -> observeData(selectedPhotoTab, mainViewModel.getFaceDetectionLiveData().getValue()));
        mainViewModel.getFaceDetectionLiveData().observe(getActivity(), hasFaceDetectionEnabled -> observeData(mainViewModel.getPhotoTabLiveData().getValue(), hasFaceDetectionEnabled));
    }

    private void observeData(Integer selectedPhotoTab, Boolean hasFaceDetectionEnabled) {
        photoViewModel.getPhotoList(selectedPhotoTab).observe(PhotoFragment.this, photoList -> {
            if (selectedPhotoTab != 0 && (hasFaceDetectionEnabled == null || !hasFaceDetectionEnabled)) {
                photoList.clear();
            }
            tvEmptyListMessage.setVisibility(photoList.isEmpty() ? View.VISIBLE : View.GONE);
            photoAdapter.submitList(photoList);
        });
    }
}
