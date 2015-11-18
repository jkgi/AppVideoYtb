package jg.videoytb.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.youtube.player.internal.p;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import java.util.List;


import jg.videoytb.adapters.AdapterVideo;
import jg.videoytb.models.VideoInfo;
import jg.videoytb.net.GetPlaylistAsyncTask;
import jg.videoytb.infinscroll.InfiniteScrollListener;

public class NvgDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String youtubePlaylist;
    private String token;
    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();

    private ListView listViewVideo;
    private AdapterVideo adapterVideo;
    List<VideoInfo> listVideoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nvg_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();

        youtubePlaylist = getString(R.string.playlist_1);
        token=null;
        listVideoInfo =null;
        listViewVideo=(ListView)findViewById(R.id.list_view_video);
        listViewVideo.setOnItemClickListener(onItemClickListener);

        setTitle(getIntent().getStringExtra("USER_NAME"));
        loadPlayList();
    }

    private void loadPlayList(){
        new GetPlaylistAsyncTask(mYoutubeDataApi, getApplicationContext()) {
            int numbersPage;
            @Override
            protected void onProgressUpdate(final Integer... values) {
                super.onProgressUpdate(values);
                numbersPage = values[0] / 10;
            }

            @Override
            public void onPostExecute(Pair<String, List<VideoInfo>> result) {
                token=result.first;
                if(listVideoInfo ==null) {
                    listVideoInfo = result.second;
                    adapterVideo=new AdapterVideo(getApplicationContext(), listVideoInfo);
                    listViewVideo.setAdapter(adapterVideo);
                    listViewVideo.setOnScrollListener(new InfiniteScrollListener(1,numbersPage) {
                        @Override
                        public void onReloadItems(int pageToRequest) {
                            loadPlayList();
                        }

                        @Override
                        public void onReloadFinished() {

                        }
                    });
                }
                else {
                    listVideoInfo.addAll(result.second);
                    adapterVideo.notifyDataSetChanged();
                }
            }
        }.execute(youtubePlaylist, token);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), YtbActivity.class);
            intent.putExtra("VIDEO_ID", listVideoInfo.get(position).getId());
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nvg_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pl_list1) {
            youtubePlaylist = getString(R.string.playlist_1);
            listVideoInfo =null;
            token=null;
            loadPlayList();
        } else if (id == R.id.pl_list2) {
            youtubePlaylist = getString(R.string.playlist_2);
            listVideoInfo =null;
            token=null;
            loadPlayList();
        } else if (id == R.id.pl_list3) {
            youtubePlaylist = getString(R.string.playlist_3);
            listVideoInfo =null;
            token=null;
            loadPlayList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
