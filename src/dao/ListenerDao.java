package dao;

import daoservices.DatabaseConnection;
import models.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListenerDao implements DaoInterface<Listener>{
    private static ListenerDao listenerDao;
    private Connection connection=DatabaseConnection.getConnection();
    private ListenerDao() throws SQLException {
    }

    public static ListenerDao getInstance() throws SQLException {
        if(listenerDao == null){
            listenerDao = new ListenerDao();
        }
        return listenerDao;
    }

    @Override
    public void create(Listener listener) throws SQLException {
        String sql = "INSERT INTO dbproiectpao.listener (id, name, email, password, hasPaid,monthlySubscription) VALUES (?, ?, ?, ?, ?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, listener.getUserId());
            statement.setString(2, listener.getUserName());
            statement.setString(3, listener.getUserEmail());
            statement.setString(4, listener.getUserPassword());
            statement.setBoolean(5, listener.getHasPaid());
            statement.setDouble(6, listener.getMonthlySubscription());
            statement.executeUpdate();
        }
    }


    @Override
    public Listener read(int id) throws SQLException {
        String sql = "SELECT * FROM dbproiectpao.listener a WHERE a.id = ?";
        ResultSet rs = null;
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            rs = statement.executeQuery();

            while (rs.next()){
                Listener a = new Listener();
                a.setUserId(rs.getInt("id"));
                a.setUserName(rs.getString("name"));
                a.setUserEmail(rs.getString("email"));
                a.setUserPassword(rs.getString("password"));
                a.setHasPaid( rs.getBoolean("hasPaid"));
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
    public void update(Listener listener,int id)  throws SQLException{
        String sql = "UPDATE dbproiectpao.listener a set a.id = ? , a.name = ?" +
                " , a.email = ? , a.password = ?, a.hasPaid=? where a.id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, listener.getUserId());
            preparedStatement.setString(2, listener.getUserName());
            preparedStatement.setString(3,listener.getUserEmail());
            preparedStatement.setString(4, listener.getUserPassword());
            preparedStatement.setBoolean(5, listener.getHasPaid());
            preparedStatement.setInt(6,id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        //cand stergem un user trebuie sa-i stergem si playlist urile si dependintele din tabelul asociativ playlist_song
        deleteListsByListenerId(id);

        String sql = "DELETE FROM dbproiectpao.listener WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    private List<Integer> getListsByListenerId(int listenerId) throws SQLException {
        List<Integer> listenerIds = new ArrayList<>();
        String sql = "SELECT id FROM dbproiectpao.playlist WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, listenerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int listId = resultSet.getInt("id");
                listenerIds.add(listId);
            }
        }
        return listenerIds;
    }

    private void deleteListsByListenerId(int listenerId) throws SQLException {
        List<Integer> listIds = getListsByListenerId(listenerId);
        // stergem din playlist_song
        String deleteListSongSql = "DELETE FROM dbproiectpao.playlist_song WHERE idPlaylist = ?";
        try (PreparedStatement deleteAlbumSongStatement = connection.prepareStatement(deleteListSongSql)) {
            for (int albumId : listIds) {
                deleteAlbumSongStatement.setInt(1, albumId);
                deleteAlbumSongStatement.executeUpdate();
            }
        }
        // Stergem playlist urile din tabelul playlist
        String deleteListsSql = "DELETE FROM dbproiectpao.playlist WHERE id = ?";
        try (PreparedStatement deleteListStatement = connection.prepareStatement(deleteListsSql)) {
            for (int listId : listIds) {
                deleteListStatement.setInt(1, listId);
                deleteListStatement.executeUpdate();
            }
        }
    }

}