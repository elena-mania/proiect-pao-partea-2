package models;

import java.util.Vector;

public class Playlist {
    private int playlistId;
    private int userId;
    public Playlist(){}
    public Playlist(int userId, int playlistId) {
        this.playlistId=playlistId;
        this.userId=userId;
    }

    public int getPlaylistId() {return playlistId;}

    public void setPlaylistId(int playlistId) {this.playlistId = playlistId;}

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}

}
