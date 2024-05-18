package dao;

import daoservices.DatabaseConnection;
import models.Artist;

import javax.management.MalformedObjectNameException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistDao implements DaoInterface<Artist>{
    private static ArtistDao artistDao;
    private Connection connection=DatabaseConnection.getConnection();
    private ArtistDao() throws SQLException {
    }

    public static ArtistDao getInstance() throws SQLException {
        if(artistDao == null){
            artistDao = new ArtistDao();
        }
        return artistDao;
    }

    @Override
    public void create(Artist artist) throws SQLException {
        String sql = "INSERT INTO dbproiectpao.artist VALUES (?, ?, ?, ?, ?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artist.getUserId());
            statement.setString(2, artist.getUserName());
            statement.setString(3,artist.getUserEmail());
            statement.setString(4, artist.getUserPassword());
            statement.setString(5, artist.getGenre());
            statement.setDouble(6, artist.getMonthlyEarnings());
            statement.executeUpdate();
        }
    }
    @Override
    public Artist read(int id) throws SQLException {
        String sql = "SELECT * FROM dbproiectpao.artist a WHERE a.id = ?";
        ResultSet rs = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                Artist a = new Artist();
                a.setUserId(rs.getInt("id"));
                a.setUserName(rs.getString("name"));
                a.setUserEmail(rs.getString("email"));
                a.setUserPassword(rs.getString("password"));
                a.setGenre(rs.getString("genre"));
                a.setMonthlyEarnings(rs.getDouble("monthlyEarnings"));
                return a;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }

    @Override
    public void update(Artist artist, int id)  throws SQLException{
        String sql = "UPDATE dbproiectpao.artist a set a.id = ? , a.name = ?" +
                " , a.email = ? , a.password = ?, a.genre=?, a.monthlyEarnings=? where a.id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, artist.getUserId());
            preparedStatement.setString(2, artist.getUserName());
            preparedStatement.setString(3,artist.getUserEmail());
            preparedStatement.setString(4, artist.getUserPassword());
            preparedStatement.setString(5, artist.getGenre());
            preparedStatement.setDouble(6, monthlyEarningsCalculator(connection,id));//calculam venitul
            preparedStatement.setInt(7,id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int artistId) throws SQLException {
        //cand stergem un artist trebuie sa stergem piesele si albumele lui
        //trebuie sa stergem si legaturile din tabelele asociative album si playlist
        deleteAlbumsByArtistId(artistId);
        deleteSongsByArtistId(artistId);

        String sql = "DELETE FROM dbproiectpao.artist WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artistId);
            statement.executeUpdate();
        }

    }
    public static double monthlyEarningsCalculator(Connection connection, int idArtist) throws SQLException {
        String getIncomeSql = "SELECT monthlyEarnings FROM dbproiectpao.artist WHERE id = ?";
        String calculateEarningsSql = "SELECT COUNT(DISTINCT song.id) AS song_count, COUNT(DISTINCT playlist_song.idPlaylist) AS playlist_count " +
                "FROM dbproiectpao.song " +
                "JOIN dbproiectpao.playlist_song ON song.id = playlist_song.idSong " +
                "WHERE song.idArtist = ? " +
                "GROUP BY song.idArtist";

        double existingIncome = 0.0;
        double newIncome = 0.0;
        //gasim existing income
        try (PreparedStatement getIncomeStmt = connection.prepareStatement(getIncomeSql)) {
            getIncomeStmt.setInt(1, idArtist);
            try (ResultSet rs = getIncomeStmt.executeQuery()) {
                if (rs.next()) {
                    existingIncome = rs.getDouble("monthlyEarnings");
                }
            }
        }

        //recalculam
        try (PreparedStatement calculateEarningsStmt = connection.prepareStatement(calculateEarningsSql)) {
            calculateEarningsStmt.setInt(1, idArtist);
            try (ResultSet rs = calculateEarningsStmt.executeQuery()) {
                if (rs.next()) {
                    int songCount = rs.getInt("song_count");
                    int playlistCount = rs.getInt("playlist_count");
                    newIncome = songCount * playlistCount * 12.99; // formula pentru calculalarea noului venit
                }
            }
        }

        // le adaugam
        return existingIncome + newIncome;
    }


    private List<Integer> getSongsByArtistId(int artistId) throws SQLException {
        List<Integer> songIds = new ArrayList<>();
        String sql = "SELECT id FROM dbproiectpao.song WHERE idArtist = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artistId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int songId = resultSet.getInt("id");
                songIds.add(songId);
            }
        }
        return songIds;
    }
    private void deleteSongsByArtistId(int artistId) throws SQLException {
        List<Integer> songIds = getSongsByArtistId(artistId);
        // stergem piesele din playlist_song
        String deletePlaylistSongSql = "DELETE FROM dbproiectpao.playlist_song WHERE idSong = ?";
        try (PreparedStatement deletePlaylistSongStatement = connection.prepareStatement(deletePlaylistSongSql)) {
            for (int songId : songIds) {
                deletePlaylistSongStatement.setInt(1, songId);
                deletePlaylistSongStatement.executeUpdate();
            }
        }
        //stergem melodiile artistului din tabelul song
        String deleteSongSql = "DELETE FROM dbproiectpao.song WHERE id = ?";
        try (PreparedStatement deleteSongStatement = connection.prepareStatement(deleteSongSql)) {
            for (int songId : songIds) {
                deleteSongStatement.setInt(1, songId);
                deleteSongStatement.executeUpdate();
            }
        }
        //nu e nevoie sa stergem piesele din album_song pentru ca acestea sunt sterse odata cu stergerea albumelor
    }
    private List<Integer> getAlbumsByArtistId(int artistId) throws SQLException {
        List<Integer> albumIds = new ArrayList<>();
        String sql = "SELECT id FROM dbproiectpao.album WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, artistId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int albumId = resultSet.getInt("id");
                albumIds.add(albumId);
            }
        }
        return albumIds;
    }

    private void deleteAlbumsByArtistId(int artistId) throws SQLException {
        List<Integer> albumIds = getAlbumsByArtistId(artistId);
        // stergem albumele din album_song
        String deleteAlbumSongSql = "DELETE FROM dbproiectpao.album_song WHERE idAlbum = ?";
        try (PreparedStatement deleteAlbumSongStatement = connection.prepareStatement(deleteAlbumSongSql)) {
            for (int albumId : albumIds) {
                deleteAlbumSongStatement.setInt(1, albumId);
                deleteAlbumSongStatement.executeUpdate();
            }
        }
        // Stergem albumele din tabelul album
        String deleteAlbumSql = "DELETE FROM dbproiectpao.album WHERE id = ?";
        try (PreparedStatement deleteAlbumStatement = connection.prepareStatement(deleteAlbumSql)) {
            for (int albumId : albumIds) {
                deleteAlbumStatement.setInt(1, albumId);
                deleteAlbumStatement.executeUpdate();
            }
        }
    }

}
