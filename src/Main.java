import javax.swing.*;
import ui.Application;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}