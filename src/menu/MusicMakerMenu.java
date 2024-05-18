package menu;

import services.PlaylistService;
import services.SongService;

import java.sql.SQLException;
import java.util.Scanner;

public class MusicMakerMenu {
    private static MusicMakerMenu instance;
    private PlaylistService playlistService;
    private SongService songService;
    private MusicMakerMenu() {
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
    }
    public static MusicMakerMenu getInstance() {
        if(instance==null){
            instance = new MusicMakerMenu();
        }
        return instance;
    }

    public void displayMenu() {
        System.out.println("Welcome to the Music Maker Menu! What do you wish to do?");
        System.out.println("1. See your albums");
        System.out.println("2. Add album");
        System.out.println("3. Remove album");
        System.out.println("4. Update album");
        System.out.println("5. Create song");
        System.out.println("6. Update song");
        System.out.println("7. Delete song");
        System.out.println("8. Add a song to a playlist");
        System.out.println("9. Exit");
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
                    playlistService.update();
                    break;
                case 5:
                    songService.create();
                    break;
                case 6:
                    songService.update();
                    break;
                case 7:
                    songService.delete();
                    break;
                case 8:
                    addSongToAlbum();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }
    private void addSongToAlbum(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Please enter your id: ");
        int id=scanner.nextInt();
        System.out.println("Please enter the id of the album you wish to add the song to: ");
        int idPlaylist=scanner.nextInt();
        System.out.println("Please enter the id of the song: ");
        int idSong=scanner.nextInt();
        playlistService.addSongToAlbum(idPlaylist,idSong,id);
    }
}