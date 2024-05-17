package daoservices;

import dao.SongDao;
import models.Song;

import java.sql.SQLException;

public class SongRepositoryService {
    private SongDao songDao=SongDao.getInstance();
    public SongRepositoryService() throws SQLException {}
    public Song getSongById(int id) throws SQLException{
        Song song=songDao.read(id);
        if(song==null)
           return null;
       return song;
    }
    public void removeSong(int id) throws SQLException {
        Song song=getSongById(id);
        if(song==null) return;
        else {
            songDao.delete(id);
        }
        System.out.println("The song: "+song.getTitle()+" was successfully removed!");
    }
    public void addSong(Song song) throws SQLException{
        if(song!=null){
            songDao.create(song);
            System.out.println("The song was successfully added!");
        }
        else System.out.println("There was an error during the process, try again!");
    }
    public void updateSong(Song song, int id) throws SQLException {
        if(song==null) System.out.println("There was an error during the process!");
        else {songDao.update(song,id);
            System.out.println("The song was successfully updated!");}
    }
}

