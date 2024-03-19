package com.vesska;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

// Diese Klasse enthält Hilfsmethoden zum Erstellen und Anzeigen von Diagrammen
// This class contains helper methods for creating and displaying charts
public class CSVChartUtils {

    // Methode zum Einrichten der Schaltfläche für das Diagramm
    // Method for setting up the button for the chart (old code)
    public static void setupChartButton(JFrame frame, DefaultTableModel model) {
        JButton chartButton = new JButton("Diagramm anzeigen");
        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPercentageChart(frame, model);
            }
        });
        JPanel buttonPanel = new JPanel(); // Panel für die Schaltfläche
        buttonPanel.add(chartButton);
        frame.add(buttonPanel, BorderLayout.SOUTH); // Hinzufügen des Panels mit der Schaltfläche zum Hauptfenster
    }

    // Methode zum Anzeigen des prozentualen Diagramms
    // Method for displaying the percentage chart
    static void showPercentageChart(JFrame frame, DefaultTableModel model) {
        // Sammeln der eindeutigen Werte und ihrer Häufigkeiten aus der sechsten Spalte
        // Collecting unique values and their frequencies from the sixth column
        HashSet<String> uniqueValues = new HashSet<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 5); // Wert aus der sechsten Spalte (Index 5) erhalten
            if (value != null) {                          // Getting value from the sixth column (index 5)
                if (value.toString().isEmpty())
                    value = "N/A";
                uniqueValues.add(value.toString());
            }
        }

        // Erstellen des Datensatzes für das Diagramm
        // Creating the dataset for the chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String value : uniqueValues) {
            int count = 0;
            for (int i = 0; i < model.getRowCount(); i++) {       // Getting value from the sixth column (index 5)
                Object cellValue = model.getValueAt(i, 5); // Wert aus der sechsten Spalte (Index 5) erhalten
                if (cellValue != null && (cellValue.toString().equals(value) || cellValue.toString().isEmpty() && value.equals("N/A"))){
                    count++;
                }
            }
            dataset.setValue(value, count); // Werte zum Diagrammdatensatz hinzufügen
        }                                   // Adding values to the chart dataset

        // Erstellen und Anzeigen des Diagramms
        JFreeChart chart = ChartFactory.createPieChart(
                "Prozentdiagramm für Spalte: " + model.getColumnName(5), // Diagrammname mit Spaltennamen
                dataset,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame chartFrame = new JFrame("Prozentdiagramm");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }
}
