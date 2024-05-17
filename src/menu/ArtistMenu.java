package menu;

import services.PlaylistService;
import services.SongService;
import services.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class ArtistMenu {
    private static ArtistMenu instance;
    private PlaylistService playlistService;
    private Scanner scanner;
    private SongService songService;
    private ArtistMenu() {
        try {
            this.playlistService=new PlaylistService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.songService=new SongService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.scanner= new Scanner(System.in);}

    public static ArtistMenu getInstance() {
        if(instance==null)
            instance=new ArtistMenu();
        return instance;
    }

    public void displayMenu() {
        System.out.println('\n');
        System.out.println("Welcome to the Artist Menu! What do you want to do?");
        System.out.println("1. Update account info");
        System.out.println("2. Delete account");
        System.out.println("3. Enter music maker menu");
        System.out.println("4. Exit");
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
                    MusicMakerMenu musicMakerMenu = MusicMakerMenu.getInstance();
                    musicMakerMenu.handleInput();
                    return;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }while(choice != 4);
    }
}
