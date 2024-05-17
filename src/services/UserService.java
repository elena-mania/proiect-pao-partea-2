package services;

import daoservices.UserRepositoryService;
import models.Artist;
import models.Listener;
import models.User;
import utils.FileManagement;
import java.sql.SQLException;
import java.util.Scanner;
import static utils.PrimaryKeyGenerator.getNextPrimaryKeyJoinTables;

public class UserService {
    private static int idUserCounter=getNextPrimaryKeyJoinTables("artist","listener","id");
    private UserRepositoryService dbService;
    private final Scanner scanner = new Scanner(System.in);
    public UserService() throws SQLException {this.dbService = new UserRepositoryService();}

    public void create() {
        System.out.println("Enter type of account you wish to create [artist/listener]:");
        String typeOfUser = scanner.nextLine().toLowerCase();
        if(!typeOfUserValidation(typeOfUser)) { return; }
        User user=createUser(typeOfUser);
        System.out.println("Congratulations, your accound has been successfully created!");
        user.setUserId(idUserCounter);
        try {
            dbService.addUser(user);
            FileManagement.fileWritingChar("audit.csv", "adaugam persoana " + user.getUserName());
        } catch (SQLException e) {
            System.out.println("Could not create " + e.getSQLState() + " " + e.getMessage());
        }
        System.out.println("Here is your unique id: "+user.getUserId()+"; remember it! you will need id later.");
    }

    public void read(int id) {
        User user=dbService.getUser(id);
        if(user!=null){
            System.out.println(user.getUserName()+" "+user.getUserEmail());
            if(user instanceof Artist) System.out.println(((Artist) user).getMonthlyEarnings());
            FileManagement.fileWritingChar("audit.csv", "citim persoana " + user.getUserName());
        } else
            System.out.println("User with ID " + id + " not found.");
    }

    public void update() {
        User user=validateUserLogin();
        if(user==null) {System.out.println("Invalid action!");return;}
        try {
            updateUserDetails(user);
            dbService.updateUser(user,user.getUserId());
            FileManagement.fileWritingChar("audit.csv", "actualizam persoana " + user.getUserName());
        }catch (SQLException e) {
            System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
        }
    }

    public void delete() {
        User user=validateUserLogin();
        if(user==null) {System.out.println("Invalid action!");return;}
        try {
            dbService.removeUser(user.getUserId());
            FileManagement.fileWritingChar("audit.csv", "stergere persoana " + user.getUserName()+ " si toate informatiile asociate cu aceasta");
        } catch (SQLException e) {
            System.out.println("Unable to perform action " + e.getSQLState() + " " + e.getMessage());
        }
    }
    public User validateUserLogin(){
        System.out.println("Enter your id: ");
        int idUser=scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter your password: ");
        String password=scanner.nextLine();
        return validateUserInstance(idUser,password);
    }

    public User validateUserInstance(int idUser, String password){
        if(dbService.getUser(idUser)!=null && dbService.getUser(idUser).getUserPassword().equals(password)){
            return dbService.getUser(idUser);
        }else return null;
    }

    private User createUser(String typeOfUser) {
        User user = new User();
        setUserInfo(user);
        if (typeOfUser.equals("artist")) {
            System.out.println("Enter genre:");
            String genre = scanner.nextLine();
            return new Artist(user.getUserName(), user.getUserEmail(), user.getUserPassword(), genre, user.getUserId());
        } else {
            return new Listener(user.getUserName(),user.getUserEmail(),user.getUserPassword(), user.getUserId());
        }
    }

    private void setUserInfo(User user) {
        System.out.println("Enter your name: ");
        user.setUserName(scanner.nextLine());
        System.out.println("Enter your email:");
        user.setUserEmail(scanner.nextLine());
        System.out.println("Enter your password:");
        user.setUserPassword(scanner.nextLine());
    }

    private void updateUserDetails(User user) {
        setUserInfo(user);
        if (user instanceof Artist) {
            System.out.println("Enter genre:");
            ((Artist) user).setGenre(scanner.nextLine());
            System.out.println("Account successfully updated!");
        } else if (user instanceof Listener) {
            System.out.println("Have you paid the monthly fee? (true/false):");
            ((Listener) user).setHasPaid(Boolean.parseBoolean(scanner.nextLine()));
            System.out.println("Account successfully updated!");
        }
    }

    private boolean typeOfUserValidation(String typeOfUser) {
        if (!typeOfUser.equals("artist") && !typeOfUser.equals("listener")) {
            System.out.println("Invalid type of user.");
            return false;
        }
        return true;
    }
}
