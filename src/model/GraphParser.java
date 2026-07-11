package model;

import java.util.*;
import java.util.regex.Pattern;

public class GraphParser {

    public Graph parse(String input) throws ParseException {
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
        int startIndex = 0;

        try {
            vertexCount = Integer.parseInt(validLines.get(0));
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

            Random rand = new Random();
            boolean v1Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex1Name));
            boolean v2Exists = graph.getVertices().stream().anyMatch(v -> v.getName().equals(vertex2Name));

            if (!v1Exists) {
                graph.addVertex(new Vertex(vertex1Name, 100 + rand.nextInt(500), 100 + rand.nextInt(400)));
                System.out.println("Добавлена вершина: " + vertex1Name);
            }
            if (!v2Exists) {
                graph.addVertex(new Vertex(vertex2Name, 100 + rand.nextInt(500), 100 + rand.nextInt(400)));
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

        System.out.println("Всего загружено вершин: " + graph.getVertices().size());
        System.out.println("Всего загружено ребер: " + graph.getEdges().size());

        return graph;
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}