package com.vesska;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diese Klasse enthält die Hauptlogik zum Anzeigen und Bearbeiten einer CSV-Tabelle
// This class contains the main logic for displaying and editing a CSV table.
public class CSVTableDisplay {
    private static DefaultTableModel model; // Modell der Tabelle
    private static JTable table; // Die Tabelle selbst
    private static final String csvFilePath = "src/main/resources/Artikel.csv"; // Pfad zur CSV-Datei

    public static void main(String[] args) {

        // Erstellen des Hauptfensters
        JFrame frame = new JFrame("CSV Tabellenanzeige");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultTableModel(); // Tabellemodell erstellen
        table = new JTable(model); // Tabelle erstellen
        JScrollPane scrollPane = new JScrollPane(table); // Scrollpane für die Tabelle erstellen
        frame.add(scrollPane, BorderLayout.CENTER); // Scrollpane zum Hauptfenster hinzufügen

        // Schaltfläche zum Hinzufügen eines neuen Datensatzes
        // Button for adding a new record
        JButton addButton = new JButton("Neuen Datensatz hinzufügen");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEmptyRow();
            }
        });

        // Schaltfläche zum Speichern der Daten in eine Datei
        // Button for saving data to a file
        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        // Schaltfläche zum Löschen ausgewählter Zeilen
        // Button for deleting selected rows
        JButton removeButton = new JButton("Ausgewählte Zeilen löschen");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedRows();
            }
        });

        // Schaltfläche zum Anzeigen des Diagramms
        // Button for displaying the chart
        JButton chartButton = new JButton("Prozentsatzdiagramm anzeigen");
        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CSVChartUtils.showPercentageChart(frame, model);
            }
        });

        JPanel buttonPanel = new JPanel(); // Panel für Schaltflächen | Panel for buttons
        buttonPanel.add(addButton); // Schaltfläche "Datensatz hinzufügen" hinzufügen
        buttonPanel.add(removeButton); // Schaltfläche "Ausgewählte Zeilen löschen" hinzufügen
        buttonPanel.add(saveButton); // Schaltfläche "Speichern" hinzufügen
        buttonPanel.add(chartButton); // Schaltfläche "Diagramm anzeigen" hinzufügen
        frame.add(buttonPanel, BorderLayout.SOUTH); // Panel mit Schaltflächen zum Hauptfenster im unteren Bereich hinzufügen

        if (CSVFileUtils.fileExists(csvFilePath)) {
            CSVFileUtils.loadData(csvFilePath, model); // Daten aus Datei laden, wenn Datei existiert
        }

        frame.pack(); // Komponenten verpacken | Packaging components
        frame.setVisible(true); // Hauptfenster anzeigen | Display main window
    }

    // Methode zum Hinzufügen einer leeren Zeile zur Tabelle
    // Method for adding an empty row to the table
    private static void addEmptyRow() {
        if (model.getColumnCount() > 0) {
            Object[] emptyRowData = new Object[model.getColumnCount()];
            model.addRow(emptyRowData); // Leere Zeile zur Tabelle hinzufügen | Add empty row to the table
        }
    }

    // Methode zum Entfernen ausgewählter Zeilen aus der Tabelle
    // Method for removing selected rows from the table
    private static void removeSelectedRows() {
        int[] selectedRows = table.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]); // Ausgewählte Zeilen aus der Tabelle entfernen
        }
    }

    // Methode zum Speichern von Daten aus der Tabelle in eine CSV-Datei
    // Method for saving data from the table to a CSV file
    private static void saveData() {
        CSVFileUtils.saveData(csvFilePath, model); // Daten aus der Tabelle in Datei speichern
    }
}
