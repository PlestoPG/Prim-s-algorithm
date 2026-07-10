package ui;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class DragNDropHandler extends TransferHandler {
    Application application;

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
            application.setStatus("Граф загружен");
            application.repaint();
            return true;
        } catch (Exception ex) {
            application.setStatus("Не удалось загрузить граф");
            return false;
        }
    }

    DragNDropHandler(Application application) {
        this.application = application;
    }
}
