package tso.mediaplayer;

public class AdItem implements ListItem {
    private String adImageUrl;
    private String adText;

    public AdItem(String adImageUrl, String adText) {
        this.adImageUrl = adImageUrl;
        this.adText = adText;
    }

    public String getAdImageUrl() {
        return adImageUrl;
    }

    public String getAdText() {
        return adText;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}

