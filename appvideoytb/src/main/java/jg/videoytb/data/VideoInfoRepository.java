package jg.videoytb.data;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import jg.videoytb.models.VideoInfo;

public class VideoInfoRepository {

    private DatabaseHelper db;
    Dao<VideoInfo, String> videoInfoIntegerDao;

    public VideoInfoRepository(Context context){
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(context);
            videoInfoIntegerDao = db.getVideoInfoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(VideoInfo videoInfo)
    {
        try {
            return videoInfoIntegerDao.create(videoInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public VideoInfo createIfNotExists(VideoInfo videoInfo){
        try{
            return videoInfoIntegerDao.createIfNotExists(videoInfo);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<VideoInfo> getVideoByPlayListId(String playListId){
        try {
            QueryBuilder<VideoInfo, String> queryBuilder = videoInfoIntegerDao.queryBuilder();
            queryBuilder.where().eq(VideoInfo.PLAYLIST_FIELD_NAME, playListId);
            PreparedQuery<VideoInfo> preparedQuery = queryBuilder.prepare();
            List<VideoInfo> videoInfoList = videoInfoIntegerDao.query(preparedQuery);
            return  videoInfoList;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int update(VideoInfo videoInfo)
    {
        try {
            return videoInfoIntegerDao.update(videoInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(VideoInfo videoInfo)
    {
        try {
            return videoInfoIntegerDao.delete(videoInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<VideoInfo> getAll()
    {
        try {
            return videoInfoIntegerDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
