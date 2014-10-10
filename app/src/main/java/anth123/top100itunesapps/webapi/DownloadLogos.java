package anth123.top100itunesapps.webapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import anth123.top100itunesapps.AppEntry;

public class DownloadLogos extends AsyncTask<Void, Void, Void> {
    private List<AppEntry> appEntries;
    private DownloadLogosListener callback;

    public DownloadLogos(List<AppEntry> appEntries, DownloadLogosListener callback) {
        this.appEntries = appEntries;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (AppEntry appEntry : appEntries) {
            try {
                Bitmap image = getBitmapFromURL(appEntry.getImageUrl());
                appEntry.setImage(image);
            } catch (IOException e) {
                callback.failure(e.getLocalizedMessage());
                return null;
            }
        }
        callback.success(appEntries);
        return null;
    }

    public static Bitmap getBitmapFromURL(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    public interface DownloadLogosListener {
        public void success(List<AppEntry> appEntries);
        public void failure(String errorMessage);
    }
}
