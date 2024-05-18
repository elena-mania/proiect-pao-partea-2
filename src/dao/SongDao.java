package dao;

import daoservices.DatabaseConnection;
import models.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongDao implements DaoInterface<Song>{
    private static SongDao songDao;
    private Connection connection= DatabaseConnection.getConnection();
    private SongDao() throws SQLException{}
    public static SongDao getInstance() throws SQLException{
        if(songDao==null){
            songDao=new SongDao();
        }
        return songDao;
    }
    @Override
    public void create(Song song) throws SQLException{
        String sql = "INSERT INTO dbproiectpao.song VALUES (?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, song.getSongId());
            statement.setInt(2, song.getIdArtist());
            statement.setDouble(3,song.getLength());
            statement.setString(4,song.getTitle());
            statement.executeUpdate();
        }}
    @Override
    public Song read(int id) throws SQLException {
        String sql ="SELECT * FROM dbproiectpao.song a WHERE a.id =?";
        ResultSet rs = null;
        try(PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, id);
            rs = statement.executeQuery();

            while (rs.next()){
                Song a = new Song();
                a.setSongId(rs.getInt("id"));
                a.setIdArtist(rs.getInt("idArtist"));
                a.setLength(rs.getDouble("length"));
                a.setTitle(rs.getString("title"));
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
    public void update(Song song, int id) throws SQLException {
        String sql = "UPDATE dbproiectpao.song a set a.id = ? , a.idArtist = ?" +
                " , a.length = ? , a.title = ? where a.id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, song.getSongId());
            preparedStatement.setInt(2, song.getIdArtist());
            preparedStatement.setDouble(3,song.getLength());
            preparedStatement.setString(4, song.getTitle());
            preparedStatement.setInt(5,id);
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void delete(int id) throws SQLException{
            String deleteSongSql = "DELETE FROM dbproiectpao.song WHERE id = ?";
            String deleteAlbumSongSql = "DELETE FROM dbproiectpao.album_song WHERE idSong = ?";
            String deletePlaylistsSongSql = "DELETE FROM dbproiectpao.playlist_song WHERE idSong = ?";

            try (PreparedStatement deleteSongStatement = connection.prepareStatement(deleteSongSql);
                 PreparedStatement deleteAlbumSongStatement = connection.prepareStatement(deleteAlbumSongSql);
                 PreparedStatement deletePlaylistsSongStatement = connection.prepareStatement(deletePlaylistsSongSql)) {
                connection.setAutoCommit(false);
                deleteAlbumSongStatement.setInt(1, id);
                deleteAlbumSongStatement.executeUpdate();
                deletePlaylistsSongStatement.setInt(1, id);
                deletePlaylistsSongStatement.executeUpdate();
                deleteSongStatement.setInt(1, id);
                deleteSongStatement.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
    }
}
