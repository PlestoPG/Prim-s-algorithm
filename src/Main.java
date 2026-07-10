import java.awt.*;
import javax.swing.*;
import ui.Application;
import ui.FontChanger;

public class Main {
    public static void main(String[] args) {
        FontChanger.apply(new Font("Gill Sans", Font.PLAIN, 16));
        SwingUtilities.invokeLater(Application::new);
    }
}