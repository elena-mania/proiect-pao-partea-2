package services;

import daoservices.SongRepositoryService;
import daoservices.UserRepositoryService;
import models.Song;
import utils.FileManagement;

import java.sql.SQLException;
import java.util.Scanner;

import static utils.Constants.AUDIT_FILE;
import static utils.PrimaryKeyGenerator.getNextPrimaryKey;

public class SongService {
    private SongRepositoryService dbSongService;
    private UserRepositoryService dbUserService;
    private final Scanner in;
    public SongService() throws SQLException {
        this.in=new Scanner(System.in);
        this.dbSongService=new SongRepositoryService();
        this.dbUserService=new UserRepositoryService();
    }
    public void create(){
        System.out.println("Enter your id: ");
        int idArtist = in.nextInt();
        in.nextLine();
        //tb sa verificam daca id-ul artistului exista in baza de date
        if ("artist".equals(userIdValidation(idArtist))) {
            System.out.println("Enter the title of the song:");
            String title = in.nextLine();
            System.out.println("Enter the length of the song: ");
            double length = in.nextDouble();
            in.nextLine();

            Song song = new Song(length, title, idArtist, getNextPrimaryKey("song","id"));
            try {
                dbSongService.addSong(song);
                FileManagement.fileWritingChar(AUDIT_FILE,"adaugam piesa "+ song.getTitle());
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
                //throw new RuntimeException(e);
            }
        } else {
            System.out.println("The input id is not an artist's id!");
        }
    }

    public void read() {
        System.out.println("Enter the song's id: ");
        int id = in.nextInt();
        in.nextLine();
        Song song = null;
        try {
            song = dbSongService.getSongById(id);
            if (song != null) {
                System.out.println("Song Information:");
                System.out.println("ID: " + song.getSongId());
                System.out.println("Title: " + song.getTitle());
                System.out.println("Length: " + song.getLength());
                System.out.println("Artist ID: " + song.getIdArtist());

                FileManagement.fileWritingChar(AUDIT_FILE, "citim piesa " + song.getTitle());
            } else {
                System.out.println("Song not found with id: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve song with id: " + id);
            //throw new RuntimeException(e);
        }
    }

    public void update() {
        //verificare pt user, pt ca numai autorul piesei o poate edita
        System.out.println("Enter your id: ");
        int idUser = in.nextInt();
        in.nextLine();
        System.out.println("Enter the song's id: ");
        int idSong = in.nextInt();
        in.nextLine();

        if (isSongAuthor(idUser, idSong)) {
            Song song = null;
            try {
                song = dbSongService.getSongById(idSong);
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
                //throw new RuntimeException(e);
            }

            System.out.println("Enter new title: ");
            String newTitle = in.nextLine();
            song.setTitle(newTitle);

            System.out.println("Enter new length: ");
            double newLength = in.nextDouble();
            in.nextLine();
            song.setLength(newLength);

            try {
                dbSongService.updateSong(song, idSong);
                FileManagement.fileWritingChar(AUDIT_FILE, "actualizam piesa " + song.getTitle());
                System.out.println("The song was successfully updated!");
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
        } else {
            System.out.println("You don't have permission to edit this song!");
        }
    }

    public void delete(){
        System.out.println("Enter your id: ");
        int idUser=in.nextInt();
        System.out.println("Enter the song's id: ");
        int idSong=in.nextInt();
        if(isSongAuthor(idUser, idSong)){
            try {
                dbSongService.removeSong(idSong);
                FileManagement.fileWritingChar(AUDIT_FILE, "stergem piesa cu id-ul "+idSong);
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
        }
        else System.out.println("Invalid action!");
    }

    private boolean isSongAuthor(int idUser, int idSong){
        try {
            if(dbSongService.getSongById(idSong).getIdArtist()==idUser)
                return true;
        } catch (SQLException e) {
            System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
        }
        return false;
    }
    private String userIdValidation(int id) {
        if (dbUserService!=null) {
            try {
                if (dbUserService.getArtistById(id)!=null){
                    return "artist";
                }else if(dbUserService.getListenerById(id)!=null){
                    return "listener";
                }
            } catch (SQLException e) {
                System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
            }
        }
        return null;
    }
}
