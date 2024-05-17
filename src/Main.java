
import menu.LoginMenu;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        LoginMenu loginMenu = LoginMenu.getInstance();
        loginMenu.handleInput();
    }
}