package menu;

import models.Artist;
import models.User;
import services.UserService;

import java.sql.SQLException;
import java.util.Scanner;
public class LoginMenu{
    private static LoginMenu instance;
    private UserService userService;
    private LoginMenu() {
        try {
            this.userService=new UserService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static LoginMenu getInstance() {
        if (instance==null)
            instance=new LoginMenu();
        return instance;
    }

    public void displayMenu() {
        System.out.println("Welcome to the Login Menu! What do you want to do?");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }
    public void handleInput() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    login();
                    return;
                case 2:
                    userService.create();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while(choice!=3);
    }
    private void login() {
        User user=userService.validateUserLogin();
        if (user!= null) {
            System.out.println("Login successful! Welcome, " +user.getUserName() + "!");
            //aici ar trb sa apelam main menu care e special pt fiecare utilizatoe in parte
            if(user instanceof Artist){
                ArtistMenu artistMenu= ArtistMenu.getInstance();
                artistMenu.handleInput(userService);
            }else{
                MainMenu mainMenu = MainMenu.getInstance();
                // apelam metoda meniu input pentru instanta userului creata cu service ul si dam si id ul ca sa lucram in continuare cu userul
                mainMenu.handleInput(userService);
            }
        } else {
            System.out.println("Invalid id or password, please try again later.");
        }
    }

}
