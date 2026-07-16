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

    public static boolean parseAndLoad(Application application, File input) {
        try {
            return parseAndLoad(application, Files.readString(input.toPath()));
        } catch (IOException ex) {
            application.setStatus("Не удалось извлечь текст из файла");
        }
        return false;
    }

    public static boolean parseAndLoad(Application application, String input) {
        boolean withResult = false;
        try {
            if (input.trim().isEmpty()) {
                application.setStatus("Получен пустой текст, граф не загружен");
                return false;
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

            int startIndex;
            try {
                Integer.parseInt(validLines.getFirst());
                startIndex = 1;
            } catch (NumberFormatException e) {
                startIndex = 0;
            }

            Pattern pattern = Pattern.compile("^\\s*(\\S+)\\s+(\\S+)\\s+(\\d+)(\\s+(\\S+))?\\s*$");

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

                boolean inMST = matcher.group(5) != null && matcher.group(5).trim().equals(Edge.State.IN_MST.name());

                boolean v1Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex1Name));
                boolean v2Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex2Name));

                if (!v1Exists) {
                    Point point = Canvas.randomPoint();
                    graph.addVertex(new Vertex(vertex1Name, point.x, point.y));
                }
                if (!v2Exists) {
                    Point point = Canvas.randomPoint();
                    graph.addVertex(new Vertex(vertex2Name, point.x, point.y));
                }

                Edge edge = graph.addEdge(vertex1Name, vertex2Name, weight);
                if (inMST) {
                    withResult = true;
                    edge.setState(Edge.State.IN_MST);
                    graph.getVertices().stream().filter(v -> v.getName().equals(vertex1Name))
                            .findFirst().ifPresent(v -> v.setState(Vertex.State.IN_MST));
                    graph.getVertices().stream().filter(v -> v.getName().equals(vertex2Name))
                            .findFirst().ifPresent(v -> v.setState(Vertex.State.IN_MST));
                }
            }

            if (graph.getEdges().isEmpty()) {
                throw new ParseException("Не найдено ни одного ребра в графе");
            }

            application.graph = graph;
            int verticesCount = graph.getVertices().size();
            int edgesCount = graph.getEdges().size();

            if (withResult) {
                application.setStatus("Загружен граф с выделенным МОД. Вершин: " + verticesCount + ", рёбер: " + edgesCount);
                application.loadedGraphWithResult();
            } else {
                application.setStatus("Загружен новый граф. Вершин: " + verticesCount + ", рёбер: " + edgesCount);
                application.graphChanged();
            }
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