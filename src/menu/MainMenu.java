package menu;

import services.PlaylistService;
import services.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
    private static MainMenu instance;
    private PlaylistService playlistService;
    private Scanner scanner;
    private MainMenu() {
        try {
            this.playlistService=new PlaylistService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.scanner= new Scanner(System.in);}

    public static MainMenu getInstance() {
        if(instance==null)
            instance=new MainMenu();
        return instance;
    }

    public void displayMenu() {
        System.out.println('\n');
        System.out.println("Welcome to the Main Menu! What do you want to do?");
        System.out.println("1. Update account info");
        System.out.println("2. Delete account");
        System.out.println("3. Enter the musicPlayer");
        System.out.println("4. See an artist's info (you must know their id)");
        System.out.println("5. Exit");
    }

    public void handleInput(UserService userService) { //ii dam serviciile userului care s a conectat la contul respectiv
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    userService.update();
                    break;
                case 2:
                    userService.delete();
                    System.exit(0);
                    break;
                case 3:
                    enterMusicPlayer();
                    break;
                case 4:
                    seeAnArtist(userService);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void seeAnArtist(UserService userService) {
        //arata informatii despre tot artistii
        System.out.println("Enter the id of the artist you want to see: ");
        int idArtist=scanner.nextInt();
        userService.read(idArtist);
    }

    private void enterMusicPlayer() {
        //acum apelam meniul de ascultat muzica;
        MusicPlayerMenu musicMenu=MusicPlayerMenu.getInstance();
        musicMenu.handleInput(playlistService);
    }
}
