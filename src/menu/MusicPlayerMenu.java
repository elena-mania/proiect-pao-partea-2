package menu;

import services.PlaylistService;

import java.sql.SQLException;
import java.util.Scanner;
public class MusicPlayerMenu {
    private static MusicPlayerMenu instance;
    private PlaylistService playlistService;
    private Scanner scanner;
    private MusicPlayerMenu() {
        try {
            this.playlistService=new PlaylistService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.scanner = new Scanner(System.in);}
    public static MusicPlayerMenu getInstance() {
        if(instance==null){
            instance = new MusicPlayerMenu();
        }
        return instance;
    }

    public void displayMenu() {
        System.out.println("Welcome to the Music Player Menu! What do you wish to do?");
        System.out.println("1. See your playlists");
        System.out.println("2. Add playlist");
        System.out.println("3. Remove playlist");
        System.out.println("4. Add a song to a playlist");
        System.out.println("5. Play playlist");
        System.out.println("6. Exit");
    }

    public void handleInput(PlaylistService playlistService) {
        this.playlistService=playlistService; ///instantiem obiectul din clasa asta cu cel din clasa mainMenu
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Please enter your id: ");
                    int id=scanner.nextInt();
                    playlistService.read(id);
                    break;
                case 2:
                    playlistService.create();
                    break;
                case 3:
                    playlistService.delete();
                    break;
                case 4:
                    addSongToPlaylist();
                    break;
                case 5:
                    System.out.println("Enter the id of the playlist you wish to listen to: ");
                    int idList=scanner.nextInt();
                    playlistService.playPlaylist(idList);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void addSongToPlaylist(){
        System.out.println("Please enter your id: ");
        int id=scanner.nextInt();
        System.out.println("Please enter the id of the playlist you wish to add the song to: ");
        int idPlaylist=scanner.nextInt();
        System.out.println("Please enter the id of the song: ");
        int idSong=scanner.nextInt();
        playlistService.addSongToList(idPlaylist,idSong,id);

    }
}
