package com.vesska;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVTableDisplay {

    private static DefaultTableModel model;
    private static JTable table;
    private static String csvFilePath = "src/main/resources/Artikel.csv";

    public static void main(String[] args) {
        JFrame frame = new JFrame("CSV Table Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add new record");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEmptyRow();
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        frame.pack();
        frame.setVisible(true);
    }

    private static void loadData() {
        try (Reader reader = new FileReader(csvFilePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Получение заголовков столбцов
            for (String header : csvParser.getHeaderNames()) {
                model.addColumn(header);
            }

            // Заполнение данных из CSV файла
            for (CSVRecord record : csvParser) {
                List<Object> rowData = new ArrayList<>();
                for (String header : csvParser.getHeaderNames()) {
                    rowData.add(record.get(header));
                }
                model.addRow(rowData.toArray());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void addEmptyRow() {
        if (model.getColumnCount() > 0) {
            Object[] emptyRowData = new Object[model.getColumnCount()];
            model.addRow(emptyRowData);
        }
    }

    private static void saveData() {
        try (FileWriter writer = new FileWriter(csvFilePath);
             CSVParser csvParser = new CSVParser(new FileReader(csvFilePath), CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Запись заголовков столбцов
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (i > 0) {
                    writer.append(';');
                }
                writer.append(model.getColumnName(i));
            }
            writer.append('\n');

            // Запись данных из таблицы
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (j > 0) {
                        writer.append(';');
                    }
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        writer.append(value.toString());
                    }
                }
                writer.append('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
