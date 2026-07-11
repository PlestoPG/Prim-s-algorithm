package ui;

import model.GraphParser;
import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DragNDropHandler extends TransferHandler {
    private final Application application;

    public DragNDropHandler(Application application) {
        this.application = application;
        System.out.println("DragNDropHandler создан (TransferHandler)");
    }

    @Override
    public boolean canImport(TransferSupport support) {
        System.out.println("canImport вызван");

        boolean canImport = support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                support.isDataFlavorSupported(DataFlavor.stringFlavor);

        System.out.println("Можно импортировать: " + canImport);

        if (canImport) {
            support.setDropAction(COPY);
        }
        return canImport;
    }

    @Override
    public boolean importData(TransferSupport support) {
        System.out.println("importData вызван");

        if (!canImport(support)) {
            System.out.println("Импорт отклонен - неподдерживаемый формат");
            return false;
        }

        try {
            Transferable transferable = support.getTransferable();
            String text = null;
            String source = "";

            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                System.out.println("Обработка файла...");
                Object data = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (data instanceof List) {
                    List<?> files = (List<?>) data;
                    if (!files.isEmpty() && files.get(0) instanceof File) {
                        File file = (File) files.get(0);
                        source = "файла: " + file.getName();
                        text = Files.readString(file.toPath());
                        System.out.println("Файл прочитан: " + file.getName());
                        System.out.println("Содержимое файла:");
                        System.out.println(text);
                    }
                }
            }
            else if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                System.out.println("Обработка текста...");
                text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                source = "текста";
                System.out.println("Текст получен: " + text);
            }

            if (text == null || text.trim().isEmpty()) {
                System.out.println("Текст пустой или null");
                application.setStatus("Нет данных для загрузки");
                return false;
            }

            System.out.println("Начинаем парсинг графа...");
            GraphParser parser = new GraphParser();
            Graph newGraph = parser.parse(text);

            application.graph = newGraph;
            application.repaint();

            int vertexCount = application.graph.getVertices().size();
            int edgeCount = application.graph.getEdges().size();
            application.setStatus("Граф загружен из " + source +
                    ". Вершин: " + vertexCount + ", рёбер: " + edgeCount);

            System.out.println("Граф загружен: вершин=" + vertexCount + ", ребер=" + edgeCount);

            if (vertexCount > 0) {
                Component[] components = application.getContentPane().getComponents();
                for (Component comp : components) {
                    if (comp instanceof Toolbar) {
                        ((Toolbar) comp).verticesAppeared();
                        break;
                    }
                }
            }

            return true;

        } catch (GraphParser.ParseException ex) {
            System.err.println("Ошибка парсинга: " + ex.getMessage());
            application.setStatus("Ошибка загрузки графа: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Ошибка загрузки графа:\n" + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        } catch (UnsupportedFlavorException | IOException ex) {
            System.err.println("Ошибка чтения: " + ex.getMessage());
            application.setStatus("Ошибка: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Не удалось загрузить граф:\n" + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.err.println("Неизвестная ошибка: " + ex.getMessage());
            ex.printStackTrace();
            application.setStatus("Ошибка: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Не удалось загрузить граф:\n" + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}