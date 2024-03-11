package com.vesska;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashSet;

public class CSVTableDisplay {

    private static DefaultTableModel model; // Модель таблицы
    private static JTable table; // Сама таблица
    private static String csvFilePath = "src/main/resources/Artikel.csv"; // Путь к CSV файлу

    public static void main(String[] args) {
        // Создание главного окна
        JFrame frame = new JFrame("CSV Table Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultTableModel(); // Создание модели таблицы
        table = new JTable(model); // Создание таблицы
        JScrollPane scrollPane = new JScrollPane(table); // Создание панели прокрутки для таблицы
        frame.add(scrollPane, BorderLayout.CENTER); // Добавление панели прокрутки на главное окно

        // Кнопка для добавления новой записи
        JButton addButton = new JButton("Add new record");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEmptyRow();
            }
        });

        // Кнопка для сохранения данных в файл
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        // Кнопка для удаления выбранных строк
        JButton removeButton = new JButton("Remove selected rows");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRows();
            }
        });

        // Кнопка для отображения диаграммы
        JButton chartButton = new JButton("Show Percentage Chart");
        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPercentageChart();
            }
        });

        JPanel buttonPanel = new JPanel(); // Панель для кнопок
        buttonPanel.add(addButton); // Добавление кнопки "Добавить запись"
        buttonPanel.add(removeButton); // Добавление кнопки "Удалить выбранные строки"
        buttonPanel.add(saveButton); // Добавление кнопки "Сохранить"
        buttonPanel.add(chartButton); // Добавление кнопки "Показать диаграмму"
        frame.add(buttonPanel, BorderLayout.SOUTH); // Добавление панели с кнопками на главное окно в нижнюю часть

        if (fileExists()) {
            loadData(); // Загрузка данных из файла, если файл существует
        }

        frame.pack(); // Упаковка компонентов
        frame.setVisible(true); // Отображение главного окна
    }

    // Проверка существования файла
    private static boolean fileExists() {
        return new File(csvFilePath).exists();
    }

    // Загрузка данных из CSV файла в таблицу
    private static void loadData() {
        try (Reader reader = new FileReader(csvFilePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            for (String header : csvParser.getHeaderNames()) {
                model.addColumn(header); // Добавление заголовков столбцов
            }

            for (CSVRecord record : csvParser) {
                Object[] rowData = new Object[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    rowData[i] = record.get(i); // Заполнение данных из CSV файла
                }
                model.addRow(rowData); // Добавление строки в таблицу
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Добавление пустой строки в таблицу
    private static void addEmptyRow() {
        if (model.getColumnCount() > 0) {
            Object[] emptyRowData = new Object[model.getColumnCount()];
            model.addRow(emptyRowData); // Добавление пустой строки в таблицу
        }
    }

    // Удаление выбранных строк из таблицы
    private static void removeSelectedRows() {
        int[] selectedRows = table.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]); // Удаление выбранных строк из таблицы
        }
    }

    // Сохранение данных из таблицы в файл CSV
    private static void saveData() {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (i > 0) {
                    writer.append(';');
                }
                writer.append(escapeSpecialCharacters(model.getColumnName(i))); // Запись заголовков столбцов
            }
            writer.append('\n');

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (j > 0) {
                        writer.append(';');
                    }
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        writer.append(escapeSpecialCharacters(value.toString())); // Запись данных из таблицы
                    }
                }
                writer.append('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Отображение диаграммы
    private static void showPercentageChart() {
        // Получаем уникальные значения и их количество из шестого столбца
        HashSet<String> uniqueValues = new HashSet<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 5); // Получаем значение из шестого столбца (индекс 5)
            if (value != null) {
                uniqueValues.add(value.toString());
            }
        }

        // Создаем датасет для диаграммы
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String value : uniqueValues) {
            int count = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                Object cellValue = model.getValueAt(i, 5); // Получаем значение из шестого столбца (индекс 5)
                if (cellValue != null && cellValue.toString().equals(value)) {
                    count++;
                }
            }
            dataset.setValue(value, count); // Добавление значений в датасет для диаграммы
        }

        // Создаем и отображаем диаграмму
        JFreeChart chart = ChartFactory.createPieChart(
                "Percentage Chart for Column: " + model.getColumnName(5), // Название диаграммы с именем столбца
                dataset,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame chartFrame = new JFrame("Percentage Chart");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    // Экранирование специальных символов для записи в CSV файл
    private static String escapeSpecialCharacters(String input) {
        if (input.contains(";") || input.contains("\"") || input.contains("\n")) {
            return "\"" + input.replaceAll("\"", "\"\"") + "\"";
        }
        return input;
    }
}
