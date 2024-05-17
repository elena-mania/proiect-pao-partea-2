package services;

import daoservices.PlaylistRepositoryService;
import daoservices.SongRepositoryService;
import daoservices.UserRepositoryService;
import models.Album;
import models.Playlist;
import models.Song;
import utils.FileManagement;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

import static utils.Constants.AUDIT_FILE;
import static utils.PrimaryKeyGenerator.getNextPrimaryKeyJoinTables;

public class PlaylistService {
    //private static int idPlaylistCounter=getNextPrimaryKeyJoinTables("playlist","album","id"); //pr initializarea playlist urilor
    private PlaylistRepositoryService dbListService;
    private UserRepositoryService dbUserService;
    private SongRepositoryService dbSongService;
    private final Scanner scanner;
    public PlaylistService() throws SQLException {
        this.scanner = new Scanner(System.in);
        this.dbListService=new PlaylistRepositoryService();
        this.dbUserService=new UserRepositoryService();
        this.dbSongService=new SongRepositoryService();
    }

    public void create() {
        System.out.println("Enter your id: ");
        int idUser=scanner.nextInt();
        if("artist".equals(userIdValidation(idUser))){
            try {
                dbListService.addList(inputAlbumInfo(idUser,0,true),"album");
                FileManagement.fileWritingChar(AUDIT_FILE,"adaugam albumul ");
                System.out.println("Album created successfully!");
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());

            }
        }else if("listener".equals(userIdValidation(idUser))){
            try {
                Playlist playlist=new Playlist(idUser,getNextPrimaryKeyJoinTables("playlist","album","id"));
                dbListService.addList(playlist,"playlist");
                FileManagement.fileWritingChar(AUDIT_FILE,"adaugam playlist-ul ");
                System.out.println("Playlist created successfully, here's the id: " +playlist.getPlaylistId());
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());

            }
        }else System.out.println("Invalid id!");
    }

    public void read(int idUser){
        if("artist".equals(userIdValidation(idUser))){
            System.out.println("Your albums are: "); //vreau ca pt fiecare album care are drept idAuthor==idUser sa afisez informatii
            List<Album> albums=dbListService.getAllAlbums();
            for(Album a: albums){
                if(a.getUserId()==idUser){
                    System.out.println("Album no. "+a.getPlaylistId()+", title: "+a.getTitle()+", released: "+a.getReleaseDate()+", genre: "+a.getGenre());
                    FileManagement.fileWritingChar(AUDIT_FILE,"citim albumul "+ a.getTitle());}
            }

        }else if("listener".equals(userIdValidation(idUser))){
            System.out.println("Your playlists are: ");
            List<Playlist> list=dbListService.getAllPlaylists();
            for(Playlist p: list){
                if(p.getPlaylistId()==idUser){
                    System.out.println("Playlist no. "+p.getPlaylistId());
                    FileManagement.fileWritingChar(AUDIT_FILE,"citim playlist-ul "+ p.getPlaylistId());}

            }
        }else System.out.println("Invalid id!");
    }

    public void update(){
        System.out.println("Enter your id: ");
        int idUser=scanner.nextInt();
        if("artist".equals(userIdValidation(idUser))){
            System.out.println("Enter the album's id: ");
            int idAlbum=scanner.nextInt();
            try {
                dbListService.updateList(inputAlbumInfo(idUser,idAlbum,false),idAlbum);
                FileManagement.fileWritingChar(AUDIT_FILE,"actualizam albumul cu id-ul "+ idAlbum);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else System.out.println("Invalid id!");
    }

    public void delete() {
        System.out.println("Enter your id: ");
        int idUser = scanner.nextInt();
        if ("artist".equals(userIdValidation(idUser))) {
            System.out.println("Enter the album's id: ");
            int idAlbum = scanner.nextInt();
            try {
                dbListService.removeList(idAlbum);
                FileManagement.fileWritingChar(AUDIT_FILE, "stergem albumul cu id-ul " + idAlbum);
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
        } else if (userIdValidation(idUser).equals("listener")) {
            System.out.println("Enter the album's id: ");
            int idList = scanner.nextInt();
            try {
                dbListService.removeList(idList);
                FileManagement.fileWritingChar(AUDIT_FILE, "stergem playlist-ul cu id-ul " + idList);
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
        } else System.out.println("Invalid id!");
    }

    //metoda care permite adaugarea de noi melodii intr-un playlist
    public void addSongToList(int idPlaylist, int idSong,int idListener) {
        Playlist playlist = null;
        try {
            playlist = dbListService.getPlaylistById(idPlaylist);
            if(playlist.getUserId()!=idListener){
                playlist=null; //o facem ca sa impedicam userul sa editeze alt playlist decat al lui
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (playlist != null) {
            Song song= null;
            try {
                song = dbSongService.getSongById(idSong);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(song!=null){
                try {
                    dbListService.addSongToPlaylist(idPlaylist, song);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Song added to playlist successfully!");
            }
        } else System.out.println("You don't have permission to edit this playlist!");
    }
    public void addSongToAlbum(int idAlbum, int idSong, int idArtist){
        Album album= null;
        try {
            album = dbListService.getAlbumById(idAlbum);
            if(album.getUserId()!=idArtist)
                album=null;
        } catch (SQLException e) {
            System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
        }
        if(album!=null){
            Song song= null;
            try {
                song = dbSongService.getSongById(idSong);
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
            if(song!=null){
                try {
                    dbListService.addSongToPlaylist(idAlbum,song);
                } catch (SQLException e) {
                    System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
                }
                System.out.println("Song added to album successfully!");
            }
        }else System.out.println("You don't have permission to edit this playlist!");
    }

    //metode pentru folosiea efectiva a musicplayer ului din cadrul unui playlist
    public void playPlaylist(int idPlaylist) {
        try {
            if(dbListService.getPlaylistById(idPlaylist)!=null){
                List<String> songs = dbListService.getSongsFromPlaylist(idPlaylist);
                if (songs != null) {
                    System.out.println("Now Playing playlist number " + idPlaylist);
                    for (String song : songs) {
                        System.out.println("Next song: " + song);
                        System.out.println("Press 'n' to skip, 'x' to stop, any other key to play");
                        String input = scanner.nextLine();
                        if ("x".equalsIgnoreCase(input)) {
                            System.out.println("Music player stopped.");
                            break; // stop pentru music player
                        } else if ("n".equalsIgnoreCase(input)) {
                            System.out.println("Song skipped.");
                            continue; //skip piesei curente
                        }
                        play(song);
                    }
                    System.out.println("Playlist ended.");
                }
            }else {
                System.out.println("Error: Playlist is empty!");
            }
        } catch (SQLException e) {
            System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());

        }
    }

    private void play(String song) {
        System.out.println("Playing song: " + song);
        // simulam redarea piesei cu ajutorul sleep
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Playback interrupted.");
        }
        System.out.println("Song ended: " + song);
    }

    ///nu ne trebuie si functie pentru playlist pt ca acolo nu avem de dat decat o singura valoare la constructor
    private Album inputAlbumInfo(int id, int idAlbum,boolean typeOfAction){
        scanner.nextLine();
        System.out.println("Enter the title: ");
        String title=scanner.nextLine();
        System.out.println("Enter the genre: ");
        String genre=scanner.nextLine();
        System.out.println("Enter the release date: ");
        String releaseDate=scanner.nextLine();
        if(typeOfAction==true) //daca functia e folosita in cadtrul metodei create, atunci asignam un id nou
            return new Album(id,getNextPrimaryKeyJoinTables("playlist","album","id"),releaseDate,genre,title);
        else return new Album(id,idAlbum,releaseDate,genre,title); //altfel apastram id-ul vechi al albumului
    }
    private String userIdValidation(int id){
        try {
            if(dbUserService.getArtistById(id)!=null)
                return "artist";
            else if(dbUserService.getListenerById(id)!=null)
                return "listener";
            else {System.out.println("You have no playlists!"); return null;}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

