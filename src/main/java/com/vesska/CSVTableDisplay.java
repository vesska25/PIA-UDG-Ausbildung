package com.vesska;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVTableDisplay {

    public static void main(String[] args) {
        String csvFilePath = "src/main/resources/Artikel.csv";

        JFrame frame = new JFrame("CSV Table Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel();

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
    }
}
