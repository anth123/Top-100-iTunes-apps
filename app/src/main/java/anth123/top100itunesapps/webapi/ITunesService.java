package anth123.top100itunesapps.webapi;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import anth123.top100itunesapps.AppEntry;

public interface ITunesService {
    @GET("/us/rss/topapplications/limit=100/json")
    void getApps(Callback<List<AppEntry>> callback);
}
