package models;

public class Song {
    private int songId;
    private double length;
    private String title;
    private int idArtist; // id ul artistului care a creat piesa
    public Song(){}
    public Song(double length, String title, int idArtist, int songId) {
        this.songId = songId;
        this.length = length;
        this.title = title;
        this.idArtist = idArtist;
    }

    public void setSongId(int songId) {this.songId = songId;}

    public int getSongId() {
        return songId;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(int idArtist) {
        this.idArtist = idArtist;
    }
}
