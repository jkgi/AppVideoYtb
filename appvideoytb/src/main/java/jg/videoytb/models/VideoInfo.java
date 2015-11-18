package jg.videoytb.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "video_info")
public class VideoInfo {

    public static final String PLAYLIST_FIELD_NAME = "playlistId";

    @DatabaseField(id = true)
    private
    String id;
    @DatabaseField
    private
    String title;
    @DatabaseField
    private
    String description;
    @DatabaseField
    private
    String duration;
    @DatabaseField
    private
    String viewCount;
    @DatabaseField
    private
    String likeCount;
    @DatabaseField
    private
    String dislikeCount;
    @DatabaseField
    private
    String imageUrl;
    @DatabaseField(columnName = PLAYLIST_FIELD_NAME)
    private
    String playlistId;
    @DatabaseField
    private
    Long position;

    public VideoInfo(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
