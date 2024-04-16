package tso.mediaplayer;

public class VideoItem implements ListItem {
    private String videoPath;
    private String videoTitle;
    private String thumbnailUrl;

    public VideoItem(String videoPath, String videoTitle) {
        this.videoPath = videoPath;
        this.videoTitle = videoTitle;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
    public String getThumbnailUrl() {return thumbnailUrl;}


    @Override
    public int getItemType() {
        return 0;
    }
}
