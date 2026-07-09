package ui;

import model.Graph;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class Application extends JFrame {
    protected final JLabel statusLabel = new JLabel();
    public Graph graph = new Graph();

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Прототип)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Панель инструментов
        Toolbar toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        // Холст с графом
        add(new Canvas(this, toolbar), BorderLayout.CENTER);

        // Панель снизу с ползунком и кнопкой "о разработчиках"
        add(new BottomPanel(this), BorderLayout.SOUTH);

        setVisible(true);

        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                        support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String text;
                    Transferable transferable = support.getTransferable();
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        List<?> files = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        File file = (File) files.getFirst();
                        text = Files.readString(file.toPath());
                    } else {
                        text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    }
                    // GraphParser parser = new GraphParser();
                    // prototype.graph = parser.parse(string);
                    setStatus("Граф загружен");
                    repaint();
                    return true;
                } catch (Exception ex) {
                    setStatus("Не удалось загрузить граф");
                    return false;
                }
            }
        });
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
