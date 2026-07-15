package model;

import ui.Application;
import ui.Canvas;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class GraphParser {
    public static String instruction ="<html><body style='width: 350px;'>" +
            "<b>Введите граф в формате:</b><br>" +
            "Первая строка - количество вершин<br>" +
            "Последующие строки - вершина1 вершина2 вес<br><br>" +
            "<b>Пример:</b><br>" +
            "4<br>" +
            "A B 1<br>" +
            "A C 4<br>" +
            "B C 2<br>" +
            "B D 5<br>" +
            "</body></html>";

    private static Graph parse(String input) throws ParseException {
        if (input == null || input.trim().isEmpty()) {
            throw new ParseException("Пустой ввод");
        }

        Graph graph = new Graph();
        String[] lines = input.split("\\r?\\n");

        List<String> validLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                validLines.add(line.trim());
            }
        }

        if (validLines.isEmpty()) {
            throw new ParseException("Нет данных для загрузки");
        }

        int vertexCount = 0;
        int startIndex;

        try {
            vertexCount = Integer.parseInt(validLines.getFirst());
            startIndex = 1;
            System.out.println("Количество вершин: " + vertexCount);
        } catch (NumberFormatException e) {
            System.out.println("Формат без указания количества вершин");
            startIndex = 0;
        }

        Pattern pattern = Pattern.compile("^\\s*(\\S+)\\s+(\\S+)\\s+(\\d+)\\s*$");
        int parsedEdges = 0;

        for (int i = startIndex; i < validLines.size(); i++) {
            String line = validLines.get(i);
            java.util.regex.Matcher matcher = pattern.matcher(line);

            if (!matcher.matches()) {
                throw new ParseException("Неверный формат строки " + (i + 1) +
                        ": " + line + "\nОжидаемый формат: вершина1 вершина2 вес");
            }

            String vertex1Name = matcher.group(1).trim();
            String vertex2Name = matcher.group(2).trim();
            int weight;

            try {
                weight = Integer.parseInt(matcher.group(3).trim());
                if (weight < 0) {
                    throw new ParseException("Вес должен быть неотрицательным числом в строке " + (i + 1));
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Неверный формат веса в строке " + (i + 1) + ": " + matcher.group(3));
            }

            boolean v1Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex1Name));
            boolean v2Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex2Name));

            if (!v1Exists) {
                Point point = Canvas.randomPoint();
                graph.addVertex(new Vertex(vertex1Name, point.x, point.y));
                System.out.println("Добавлена вершина: " + vertex1Name);
            }
            if (!v2Exists) {
                Point point = Canvas.randomPoint();
                graph.addVertex(new Vertex(vertex2Name, point.x, point.y));
                System.out.println("Добавлена вершина: " + vertex2Name);
            }

            graph.addEdge(vertex1Name, vertex2Name, weight);
            System.out.println("Добавлено ребро: " + vertex1Name + " - " + vertex2Name + " (" + weight + ")");
            parsedEdges++;
        }

        if (graph.getEdges().isEmpty()) {
            throw new ParseException("Не найдено ни одного ребра в графе");
        }

        if (vertexCount > 0 && graph.getVertices().size() != vertexCount) {
            System.out.println("Предупреждение: указано " + vertexCount +
                    " вершин, но загружено " + graph.getVertices().size());
        }

        System.out.println("Всего загружено вершин: " + vertexCount);
        System.out.println("Всего загружено ребер: " + parsedEdges);

        return graph;
    }

    public static boolean parseAndLoad(Application application, File input) {
        try {
            return parseAndLoad(application, Files.readString(input.toPath()));
        } catch (IOException ex) {
            application.setStatus("Не удалось извлечь текст из файла");
        }
        return false;
    }

    public static boolean parseAndLoad(Application application, String input) {
        try {
            if (input.trim().isEmpty()) {
                application.setStatus("Получен пустой текст, граф не загружен");
                return false;
            }

            application.graph = parse(input);
            int vertexCount = application.graph.getVertices().size();
            int edgeCount = application.graph.getEdges().size();

            application.setStatus("Загружен новый граф. Вершин: " + vertexCount + ", рёбер: " + edgeCount);
            application.graphChanged();
            return true;
        } catch (ParseException ex) {
            application.setStatus("Ошибка загрузки графа: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Ошибка загрузки графа:\n" + ex.getMessage() + "\n\n" + instruction,
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }  catch (Exception ex) {
            application.setStatus("Ошибка: " + ex.getMessage());
            return false;
        }
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}