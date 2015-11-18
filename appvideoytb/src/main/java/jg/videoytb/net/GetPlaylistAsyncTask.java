package jg.videoytb.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jg.videoytb.data.VideoInfoRepository;
import jg.videoytb.models.VideoInfo;

public abstract class GetPlaylistAsyncTask extends AsyncTask<String, Integer, Pair<String, List<VideoInfo>>> {
    private static final String TAG = "GetPlaylistAsyncTask";
    private static final Long YOUTUBE_PLAYLIST_MAX_RESULTS = 10L;

    private static final String YOUTUBE_PLAYLIST_PART = "snippet";
    private static final String YOUTUBE_PLAYLIST_FIELDS = "pageInfo,nextPageToken,items(id,snippet(resourceId/videoId))";

    private static final String YOUTUBE_VIDEOS_PART = "snippet,contentDetails,statistics";
    private static final String YOUTUBE_VIDEOS_FIELDS = "items(id,snippet(title,description,thumbnails/high),contentDetails/duration,statistics)";

    private static final DecimalFormat sFormatter = new DecimalFormat("#,###,###");

    private YouTube mYouTubeDataApi;
    Context context;

    public GetPlaylistAsyncTask(YouTube api, Context context) {
        mYouTubeDataApi = api;
        this.context = context;
    }

    @Override
    protected Pair<String, List<VideoInfo>> doInBackground(String... params) {
        VideoInfoRepository videoInfoRepository = new VideoInfoRepository(context);
        List<VideoInfo> videoInfoList = new ArrayList<>();
        final String playlistId = params[0];
        final String nextPageToken;

        if (params.length == 2) {
            nextPageToken = params[1];
        } else {
            nextPageToken = null;
        }

        if(!isOnline(context)){
            videoInfoList = videoInfoRepository.getVideoByPlayListId(playlistId);
            publishProgress(0);
            return new Pair(null, videoInfoList);
        }

        PlaylistItemListResponse playlistItemListResponse;
        try {
            playlistItemListResponse = mYouTubeDataApi.playlistItems()
                    .list(YOUTUBE_PLAYLIST_PART)
                    .setPlaylistId(playlistId)
                    .setPageToken(nextPageToken)
                    .setFields(YOUTUBE_PLAYLIST_FIELDS)
                    .setMaxResults(YOUTUBE_PLAYLIST_MAX_RESULTS)
                    .setKey(ApiKey.YOUTUBE_API_KEY)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (playlistItemListResponse == null) {
            Log.e(TAG, "Failed to get playlist");
            return null;
        }

        List<String> videoIds = new ArrayList();

        for (PlaylistItem item : playlistItemListResponse.getItems()) {
            videoIds.add(item.getSnippet().getResourceId().getVideoId());
        }

        VideoListResponse videoListResponse = null;
        try {
            videoListResponse = mYouTubeDataApi.videos()
                    .list(YOUTUBE_VIDEOS_PART)
                    .setFields(YOUTUBE_VIDEOS_FIELDS)
                    .setKey(ApiKey.YOUTUBE_API_KEY)
                    .setId(TextUtils.join(",", videoIds)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        publishProgress(playlistItemListResponse.getPageInfo().getTotalResults());


        VideoInfo videoInfo;
        Video video;
        PlaylistItem item;


        for(int i=0;i< videoListResponse.getItems().size(); i++){
            videoInfo = new VideoInfo();
            item = playlistItemListResponse.getItems().get(i);
            video=videoListResponse.getItems().get(i);
            videoInfo.setTitle(video.getSnippet().getTitle());
            videoInfo.setDescription(video.getSnippet().getDescription());
            videoInfo.setDuration(parseDuration(video.getContentDetails().getDuration()));
            videoInfo.setViewCount(sFormatter.format(video.getStatistics().getViewCount()));
            videoInfo.setLikeCount(sFormatter.format(video.getStatistics().getLikeCount()));
            videoInfo.setDislikeCount(sFormatter.format(video.getStatistics().getDislikeCount()));
            videoInfo.setPosition(item.getSnippet().getPosition());
            videoInfo.setPlaylistId(playlistId);
            videoInfo.setImageUrl(video.getSnippet().getThumbnails().getHigh().getUrl());
            videoInfo.setId(item.getId());

            videoInfoList.add(videoInfo);
            videoInfoRepository.createIfNotExists(videoInfo);
        }

        return new Pair(playlistItemListResponse.getNextPageToken(), videoInfoList);
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private String parseDuration(String in) {
        boolean hasSeconds = in.indexOf('S') > 0;
        boolean hasMinutes = in.indexOf('M') > 0;

        String s;
        if (hasSeconds) {
            s = in.substring(2, in.length() - 1);
        } else {
            s = in.substring(2, in.length());
        }

        String minutes = "0";
        String seconds = "00";

        if (hasMinutes && hasSeconds) {
            String[] split = s.split("M");
            minutes = split[0];
            seconds = split[1];
        } else if (hasMinutes) {
            minutes = s.substring(0, s.indexOf('M'));
        } else if (hasSeconds) {
            seconds = s;
        }

        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }

        return minutes + ":" + seconds;
    }

}
