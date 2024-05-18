package dao;

import daoservices.DatabaseConnection;
import models.*;
import static dao.ArtistDao.monthlyEarningsCalculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDao implements DaoInterface<Playlist>{
    private static PlaylistDao playlistDao;
    private Connection connection= DatabaseConnection.getConnection();
    private PlaylistDao()throws SQLException {}
    public static PlaylistDao getInstance() throws SQLException {
        if(playlistDao == null){
            playlistDao = new PlaylistDao();
        }
        return playlistDao;
    }
    @Override
    public void create(Playlist playlist) throws SQLException {
        String sql = "INSERT INTO dbproiectpao.playlist VALUES (?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playlist.getPlaylistId());
            statement.setInt(2, playlist.getUserId());
            statement.executeUpdate();
        }
    }
    @Override
    public Playlist read(int id) throws SQLException{
        String sql = "SELECT * FROM dbproiectpao.playlist a WHERE a.id = ?";
        ResultSet rs = null;
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            rs = statement.executeQuery();

            while (rs.next()){
                Playlist a = new Playlist();
                a.setPlaylistId(rs.getInt("id"));
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
    public void update(Playlist playlist, int id) throws SQLException{
        String sql = "UPDATE dbproiectpao.playlist a set a.id = ? , a.userId = ?" +
                " , where a.id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, playlist.getPlaylistId());
            preparedStatement.setInt(2, playlist.getUserId());
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void delete(int id) throws SQLException {

        String deletePlaylistSongsSql = "DELETE FROM dbproiectpao.playlist_song WHERE idPlaylist = ?";
        try (PreparedStatement statement = connection.prepareStatement(deletePlaylistSongsSql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }

        String sql = "DELETE FROM dbproiectpao.playlist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    public void addSongToPlaylist(int playlistId, int songId) throws SQLException {
        String insertSql = "INSERT INTO dbproiectpao.playlist_song (idPlaylist, idSong) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }

        // gasim id-ul artistului care a facut piesa pentru a-i putea modifica monthlyEarnings
        String selectArtistSql = "SELECT idArtist FROM dbproiectpao.song WHERE id = ?";
        int artistId;
        try (PreparedStatement statement = connection.prepareStatement(selectArtistSql)) {
            statement.setInt(1, songId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    artistId = resultSet.getInt("idArtist");
                } else {
                    throw new SQLException("Song not found for id: " + songId);
                }
            }
        }
        //Recalculam
        double newEarnings = monthlyEarningsCalculator(connection, artistId);

        // Actualizam baza de date
        String updateEarningsSql = "UPDATE dbproiectpao.artist SET monthlyEarnings = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateEarningsSql)) {
            statement.setDouble(1, newEarnings);
            statement.setInt(2, artistId);
            statement.executeUpdate();
        }
    }
    public List<String> getSongsFromPlaylist(int playlistId) throws SQLException {
        List<String> songs = new ArrayList<>();
        String sql = "SELECT title " +
                "FROM dbproiectpao.song " +
                "INNER JOIN dbproiectpao.playlist_song ON dbproiectpao.song.id = dbproiectpao.playlist_song.idSong " +
                "WHERE dbproiectpao.playlist_song.idPlaylist = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                songs.add(title);
            }
        }
        return songs;
    }

    public List<Playlist> getPlaylists() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM dbproiectpao.playlist";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs= statement.executeQuery()) {
            while (rs.next()) {
                Playlist playlist = new Playlist();
                playlist.setPlaylistId(rs.getInt("id"));
                playlist.setUserId(rs.getInt("userId"));
                playlists.add(playlist);
            }
        }
        return playlists;
    }

}
