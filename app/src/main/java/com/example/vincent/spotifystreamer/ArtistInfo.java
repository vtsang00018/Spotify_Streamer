package com.example.vincent.spotifystreamer;

/**
 * Created by Vincent on 6/29/2015.
 */
public class ArtistInfo {

    private String artistId;
    private String name;
    private String imageUrl;

    public ArtistInfo(String name, String imageUrl, String artistId) {
        this.artistId = artistId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
