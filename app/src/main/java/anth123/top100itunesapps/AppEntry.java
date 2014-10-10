package anth123.top100itunesapps;

import android.graphics.Bitmap;

public class AppEntry {
    private String name;
    private String imageUrl;
    private String price;
    private Bitmap image;

    public AppEntry(String name, String imageUrl, String price) {
        super();
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public AppEntry(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
