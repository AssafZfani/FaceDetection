package zfani.assaf.face_detection.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class PhotoModel {

    private final String imagePath;
    private final boolean hasFace;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public PhotoModel(String imagePath, boolean hasFace) {
        this.imagePath = imagePath;
        this.hasFace = hasFace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean hasFace() {
        return hasFace;
    }
}
