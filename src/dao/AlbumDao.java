package dao;

import daoservices.DatabaseConnection;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlbumDao implements DaoInterface<Album>{
    private static AlbumDao albumDao;
    private Connection connection= DatabaseConnection.getConnection();
    private AlbumDao()throws SQLException {}
    public static AlbumDao getInstance() throws SQLException {
        if(albumDao == null){
            albumDao = new AlbumDao();
        }
        return albumDao;
    }
    @Override
    public void create(Album album) throws SQLException {
        String sql = "INSERT INTO dbproiectpao.album VALUES (?,?,?,?,?);";

        try(PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, album.getPlaylistId());
            statement.setString(2,album.getReleaseDate());
            statement.setString(3,album.getGenre());
            statement.setString(4, album.getTitle());
            statement.setInt(5,album.getUserId());
            statement.executeUpdate();
        }
    }
    @Override
    public Album read(int id) throws SQLException{
        String sql = "SELECT * FROM dbproiectpao.album a WHERE a.id = ?";
        ResultSet rs = null;
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            rs = statement.executeQuery();

            while (rs.next()){
                Album a = new Album();
                a.setPlaylistId(rs.getInt("id"));
                a.setReleaseDate(rs.getString("releaseDate"));
                a.setGenre(rs.getString("genre"));
                a.setTitle(rs.getString("title"));
                a.setUserId(rs.getInt("userId"));
                return  a;
            }
        }finally {
            if(rs != null) {
                rs.close();
            }
        }
        return null;
    }
    @Override
    public void update(Album album, int id) throws SQLException{
        String sql = "UPDATE dbproiectpao.album a set a.id = ? , a.releaseDate=?, a.genre=?, a.title=?, a.userId = ?" +
                " , where a.id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, album.getPlaylistId());
            preparedStatement.setString(2, album.getReleaseDate());
            preparedStatement.setString(3,album.getGenre());
            preparedStatement.setString(4,album.getTitle());
            preparedStatement.setInt(5, album.getUserId());
            preparedStatement.setInt(6,id);
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void delete(int id) throws SQLException {
        String deletePlaylistSongsSql = "DELETE FROM dbproiectpao.album_song WHERE idAlbum = ?";
        try (PreparedStatement statement = connection.prepareStatement(deletePlaylistSongsSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        String sql = "DELETE FROM dbproiectpao.album WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    public List<Album> getAlbums() throws SQLException {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM dbproiectpao.album";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs= statement.executeQuery()) {
            while (rs.next()) {
                Album album = new Album();
                album.setPlaylistId(rs.getInt("id"));
                album.setReleaseDate(rs.getString("releaseDate"));
                album.setGenre(rs.getString("genre"));
                album.setTitle(rs.getString("title"));
                album.setUserId(rs.getInt("userId"));
                albums.add(album);
            }
        }
        return albums;
    }
    public void addSongToAlbum(int albumId, int songId) throws SQLException {
        String sql = "INSERT INTO dbproiectpao.album_song (idAlbum, idSong) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, albumId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }
    }

}
