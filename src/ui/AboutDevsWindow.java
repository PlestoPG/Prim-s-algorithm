package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AboutDevsWindow extends JPanel {
    AboutDevsWindow() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        URL photoURL;

        add(new JLabel("Бригада №8 (5)"));

        JPanel member1 = new JPanel(new BorderLayout());
        photoURL = getClass().getResource("/media/Kirill.jpg");
        if (photoURL != null)
            member1.add(new JLabel(new ImageIcon(photoURL)), BorderLayout.NORTH);
        member1.add(new JLabel("Рогачевский Кирилл Андреевич 4344"), BorderLayout.CENTER);
        add(member1);

        JPanel member2 = new JPanel(new BorderLayout());
        photoURL = getClass().getResource("/media/Marina.jpg");
        if (photoURL != null)
            member2.add(new JLabel(new ImageIcon(photoURL)), BorderLayout.NORTH);
        member2.add(new JLabel("Захарова Марина Игоревна 4344"), BorderLayout.CENTER);
        add(member2);

        JPanel member3 = new JPanel(new BorderLayout());
        photoURL = getClass().getResource("/media/Amirkhan.jpg");
        if (photoURL != null)
            member3.add(new JLabel(new ImageIcon(photoURL)), BorderLayout.NORTH);
        member3.add(new JLabel("Кяримов Амирхан Икрам Оглы 4381"), BorderLayout.CENTER);
        add(member3);
    }
}
