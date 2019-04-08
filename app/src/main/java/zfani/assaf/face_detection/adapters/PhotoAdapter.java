package zfani.assaf.face_detection.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.face_detection.R;
import zfani.assaf.face_detection.models.PhotoModel;

public class PhotoAdapter extends ListAdapter<PhotoModel, PhotoAdapter.PhotoViewHolder> {

    public PhotoAdapter() {
        super(new DiffUtil.ItemCallback<PhotoModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull PhotoModel oldItem, @NonNull PhotoModel newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull PhotoModel oldItem, @NonNull PhotoModel newItem) {
                return oldItem.getImagePath().equalsIgnoreCase(newItem.getImagePath()) && oldItem.hasFace() == newItem.hasFace();
            }
        });
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(View.inflate(parent.getContext(), R.layout.row_photo, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String imagePath = getItem(position).getImagePath();
        if (imagePath != null) {
            Glide.with(holder.ivPhoto.getContext()).load(imagePath).into(holder.ivPhoto);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
