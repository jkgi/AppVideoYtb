package jg.videoytb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import jg.videoytb.models.VideoInfo;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db.db";

    private static final int DATABASE_VERSION = 1;


    private Dao<VideoInfo, String> videoInfoDao = null;
    private RuntimeExceptionDao<VideoInfo, String> videoInfoRuntimeDaom = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, VideoInfo.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }


        RuntimeExceptionDao<VideoInfo, String> dao = getVideoInfoDataDao();

        VideoInfo videoInfo = new VideoInfo();
        dao.create(videoInfo);
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, VideoInfo.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<VideoInfo, String> getVideoInfoDao() throws SQLException {
        if (videoInfoDao == null) {
            videoInfoDao = getDao(VideoInfo.class);
        }
        return videoInfoDao;
    }

    public RuntimeExceptionDao<VideoInfo, String> getVideoInfoDataDao() {
        if (videoInfoRuntimeDaom == null) {
            videoInfoRuntimeDaom = getRuntimeExceptionDao(VideoInfo.class);
        }
        return videoInfoRuntimeDaom;
    }

    @Override
    public void close() {
        super.close();
        videoInfoDao = null;
    }

}
