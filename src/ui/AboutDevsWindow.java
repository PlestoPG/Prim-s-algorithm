package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AboutDevsWindow extends JPanel {
    int width;
    int height;
    int min;

    private JPanel appendMember(String path, String name) {
        JPanel member = new JPanel(new BorderLayout());
        URL imageURL = getClass().getResource(path);
        if (imageURL != null) {
            ImageIcon imageIcon = new ImageIcon(imageURL);
            if (!(imageIcon.getIconWidth() <= min && imageIcon.getIconHeight() <= min)) {
                double scale = Math.min(
                        (double) min / imageIcon.getIconWidth(),
                        (double) min / imageIcon.getIconHeight()
                );
                int newWidth = (int) (imageIcon.getIconWidth() * scale);
                int newHeight = (int) (imageIcon.getIconHeight() * scale);

                Image image = imageIcon.getImage().getScaledInstance(
                        newWidth,
                        newHeight,
                        Image.SCALE_SMOOTH
                );
                member.add(new JLabel(new ImageIcon(image)), BorderLayout.NORTH);
            } else {
                JLabel image = new JLabel(imageIcon);
                member.add(image, BorderLayout.NORTH);
            }
        }
        member.add(new JLabel(name, SwingConstants.CENTER), BorderLayout.CENTER);
        add(member);
        return member;
    }

    AboutDevsWindow(Application application) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
                gd.getDefaultConfiguration());
        this.width = bounds.width - insets.left - insets.right;
        this.height = bounds.height - insets.top - insets.bottom;
        this.min = Math.min(bounds.width, bounds.height) - 100;

        add(new JLabel("Бригада №5"));
        add(appendMember("/media/Kirill.jpg", "Рогачевский Кирилл Андреевич 4344"));
        add(appendMember("/media/Marina.jpg", "Захарова Марина Игоревна 4344"));
        add(appendMember("/media/Amirkhan.jpg", "Кяримов Амирхан Икрам Оглы 4381"));

        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setPreferredSize(new Dimension(min + 100, height - 100));
        JOptionPane.showMessageDialog(application, scrollPane, "О разработчиках", JOptionPane.INFORMATION_MESSAGE);
    }
}
