package com.vesska;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

        JButton removeButton = new JButton("Remove selected rows");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRows();
            }
        });

        JButton chartButton = new JButton("Show Percentage Chart");
        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPercentageChart();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(chartButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Check if the file exists
        if (fileExists()) {
            loadData();
        }

        frame.pack();
        frame.setVisible(true);
    }

    private static boolean fileExists() {
        return new File(csvFilePath).exists();
    }

    private static void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            if ((line = reader.readLine()) != null) {
                String[] headers = parseCSVLine(line);
                for (String header : headers) {
                    model.addColumn(header);
                }
            }
            while ((line = reader.readLine()) != null) {
                String[] data = parseCSVLine(line);
                model.addRow(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        boolean insideQuotes = false;
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (!escaped) {
                    insideQuotes = !insideQuotes;
                }
                escaped = false;
            } else if (c == '\\' && insideQuotes) {
                escaped = true;
            } else if (c == ';' && !insideQuotes) {
                tokens.add(token.toString().trim());
                token.setLength(0);
            } else if (c == '\n' && insideQuotes) {
                // Replace newline character with space inside quotes
                token.append(' ');
            } else {
                token.append(c);
            }
        }
        tokens.add(token.toString().trim());
        return tokens.toArray(new String[0]);
    }

    private static void addEmptyRow() {
        if (model.getColumnCount() > 0) {
            Object[] emptyRowData = new Object[model.getColumnCount()];
            model.addRow(emptyRowData);
        }
    }

    private static void removeSelectedRows() {
        int[] selectedRows = table.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);
        }
    }

    private static void saveData() {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            // Write column headers
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (i > 0) {
                    writer.append(';');
                }
                writer.append(model.getColumnName(i));
            }
            writer.append('\n');

            // Write data from the table
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

    private static void showPercentageChart() {
        // TODO: Implement chart display
        JOptionPane.showMessageDialog(null, "Percentage chart functionality will be implemented soon!");
    }
}
