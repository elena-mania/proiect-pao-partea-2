package daoservices;

import dao.AlbumDao;
import dao.PlaylistDao;
import models.Album;
import models.Playlist;
import models.Song;

import java.sql.SQLException;
import java.util.List;

public class PlaylistRepositoryService {
    private PlaylistDao playlistDao=PlaylistDao.getInstance();
    private AlbumDao albumDao=AlbumDao.getInstance();

    public PlaylistRepositoryService() throws SQLException {}

    public List<Album> getAllAlbums(){
        try {
            return albumDao.getAlbums();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Playlist> getAllPlaylists(){
        try {
            return playlistDao.getPlaylists();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSongToPlaylist(int idList, Song song) throws SQLException {
        if(getList(idList) instanceof Album){
            albumDao.addSongToAlbum(idList,song.getSongId());
        }else playlistDao.addSongToPlaylist(idList,song.getSongId());
    }

    public Playlist getPlaylistById(int id) throws SQLException {
        Playlist playlist = playlistDao.read(id);
        if (playlist != null) return playlist;
        else return null;
    }
    public List<String> getSongsFromPlaylist(int id)throws SQLException{
        return playlistDao.getSongsFromPlaylist(id);
    }
    public Album getAlbumById(int id) throws SQLException {
        Album album=albumDao.read(id);
        if(album!=null) return album;
        else return null;
    }
    public void addList(Playlist list, String type) throws SQLException{
        if(list!=null){
            switch (type){
                case "playlist"-> playlistDao.create(list);
                case "album" -> albumDao.create((Album) list);
                default -> throw new IllegalStateException("Unexpected value: " + list);
            }
        }
    }
    public void removeList(int id)throws SQLException{
        Playlist list=getList(id);
        if(list==null) return;
        if(list instanceof Album) {
            albumDao.delete(id);  System.out.println("Album " + ((Album) list).getTitle() + " was successfully removed!");
        } else if(list instanceof Playlist){
            playlistDao.delete(id);
            System.out.println("Playlist successfully removed!");
        } else {
            throw new IllegalStateException("Unexpected list type: " + list);
        }
    }
    public void updateList(Playlist list, int id) throws SQLException{
        if (list instanceof Album) {
            albumDao.update((Album) list, id);
            System.out.println("Album successfully updated!");
        } else if (list instanceof Playlist) {
            playlistDao.update(list, id);
            System.out.println("Playlist successfully updated!");
        } else {
            System.out.println("No list found!");
        }
    }
    public Playlist getList(int id){
        Playlist list = null;
        try {
            list = getPlaylistById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (list != null) {
            return list;
        }
        Album album= null;
        try {
            album = getAlbumById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (album!= null) {
            return album;
        }
        System.out.println("There is no user having id: " + id);
        return null;
    }

}
