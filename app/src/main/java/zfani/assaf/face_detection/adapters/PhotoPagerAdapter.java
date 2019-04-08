package zfani.assaf.face_detection.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import zfani.assaf.face_detection.views.PhotoFragment;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] tabTitleList;

    public PhotoPagerAdapter(@NonNull FragmentManager fm, String[] tabTitleList) {
        super(fm);
        this.tabTitleList = tabTitleList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new PhotoFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList[position];
    }
}
