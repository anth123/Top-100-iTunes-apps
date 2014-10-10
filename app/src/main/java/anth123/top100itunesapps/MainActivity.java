package anth123.top100itunesapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import anth123.top100itunesapps.webapi.AppEntriesDeserializer;
import anth123.top100itunesapps.webapi.DownloadLogos;
import anth123.top100itunesapps.webapi.ITunesService;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class MainActivity extends Activity implements OnRefreshListener   {

    private PullToRefreshLayout mPullToRefreshLayout;
    private ITunesService iTunesService;
    private ListView listView;
    private AppEntryListAdapter adapter;
    private EntriesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        database = new EntriesDatabase(this);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<List<AppEntry>>(){}.getType(),
                        new AppEntriesDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://itunes.apple.com")
                .setConverter(new GsonConverter(gson))
                .build();

        iTunesService = restAdapter.create(ITunesService.class);
        populateListView();
    }

    private void populateListView() {
        mPullToRefreshLayout.setRefreshing(true);
        List<AppEntry> entries = database.getEntries();
        if (entries.size() > 0) {
            updateListView(entries);
            mPullToRefreshLayout.setRefreshComplete();
        } else {
            onRefreshStarted(null);
        }
    }

    private void updateListView(List<AppEntry> appEntries) {
        adapter = new AppEntryListAdapter(this, appEntries);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefreshStarted(View view) {
        iTunesService.getApps(new Callback<List<AppEntry>>() {

            @Override
            public void success(List<AppEntry> appEntries, Response response) {
                downloadLogos(appEntries);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(
                        getApplicationContext(),
                        "Error: " + error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                mPullToRefreshLayout.setRefreshComplete();
            }
        });
    }

    private void downloadLogos(List<AppEntry> appEntries) {
        DownloadLogos downloadLogosTask = new DownloadLogos(appEntries, new DownloadLogos.DownloadLogosListener() {
            @Override
            public void success(final List<AppEntry> appEntries) {
                database.replaceEntries(appEntries);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateListView(appEntries);
                        mPullToRefreshLayout.setRefreshComplete();

                    }
                });
            }

            @Override
            public void failure(final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                "Error: " + errorMessage,
                                Toast.LENGTH_LONG).show();
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                });
            }
        });
        downloadLogosTask.execute();
    }

}
