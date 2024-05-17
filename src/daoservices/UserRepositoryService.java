package daoservices;

import dao.ArtistDao;
import dao.ListenerDao;
import models.Artist;
import models.Listener;
import models.User;

import java.sql.SQLException;

public class UserRepositoryService {
    private ArtistDao artistDao=ArtistDao.getInstance();
    private ListenerDao listenerDao=ListenerDao.getInstance();
    public UserRepositoryService() throws SQLException {}

    public Listener getListenerById(int id) throws SQLException{
        Listener listener = listenerDao.read(id);
        if(listener != null){
            return listener;
        }else return null;
    }

    public Artist getArtistById(int id) throws SQLException{
        Artist artist = artistDao.read(id);
        if(artist != null){
            return artist;
        }else return null;
    }

    public void removeUser(int id) throws SQLException{
        User user=getUser(id);
        if(user==null) return;
        if(user instanceof Artist) {
            artistDao.delete(id);
        } else if(user instanceof Listener){
            listenerDao.delete(id);
        } else {
            throw new IllegalStateException("Unexpected user type: " + user);
        }

        System.out.println("User " + user.getUserName() + " was successfully removed!");
    }


    public void addUser(User user) throws SQLException {
        if(user!=null){
            switch (user){
                case Artist artist-> artistDao.create(artist);
                case Listener listener -> listenerDao.create(listener);
                default -> throw new IllegalStateException("Unexpected value: " + user);
            }
        }
    }

    public void updateUser(User user, int id) throws SQLException {
        if(user!=null){
            switch (user){
                case Artist artist-> artistDao.update(artist,id);
                case Listener listener -> listenerDao.update(listener,id);
                default -> throw new IllegalStateException("Unexpected value: " + user);
            }
        }
    }
    public User getUser(int id) {
        User user = null;
        try {
            user = getArtistById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            return user;
        }
        try {
            user = getListenerById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            return user;
        }
        System.out.println("There is no user having id: " + id);
        return null;
    }

    
}
