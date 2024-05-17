package models;

public class Album extends Playlist{
    private String releaseDate, genre, title;
    //private List<Song> songs;
    public Album(){}
    public Album(int idUser, int idAlbum, String releaseDate, String genre, String title){
        super(idUser,idAlbum); this.releaseDate=releaseDate; this.genre=genre; this.title=title;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
