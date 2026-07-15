package ui;

import model.GraphParser;
import ui.toolbar.Toolbar;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DragNDropHandler extends TransferHandler {
    Application application;
    Toolbar toolbar;

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        Transferable transferable = support.getTransferable();
        try {
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List<?> files = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                File file = (File) files.getFirst();
                return GraphParser.parseAndLoad(application, file);
            } else {
                return GraphParser.parseAndLoad(application, (String) transferable.getTransferData(DataFlavor.stringFlavor));
            }
        } catch (IOException ex) {
            application.setStatus("Приложенный файл больше не доступен");
            return false;
        } catch (UnsupportedFlavorException ex) {
            application.setStatus("Приложен неподдерживаемый тип файла");
            return false;
        }
    }

    DragNDropHandler(Application application, Toolbar toolbar) {
        this.application = application;
        this.toolbar = toolbar;
    }
}
