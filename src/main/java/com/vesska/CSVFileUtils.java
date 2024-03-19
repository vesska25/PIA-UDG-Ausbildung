package com.vesska;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.table.DefaultTableModel;
import java.io.*;

// Diese Klasse enthält Hilfsmethoden zum Lesen und Schreiben von CSV-Dateien
// This class contains helper methods for reading and writing CSV files
public class CSVFileUtils {

    // Methode zum Laden von Daten aus einer CSV-Datei in ein Tabellenmodell
    // Method for loading data from a CSV file into a table model
    public static void loadData(String csvFilePath, DefaultTableModel model) {
        try (Reader reader = new FileReader(csvFilePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Hinzufügen von Spaltenüberschriften zum Tabellenmodell
            // Adding column headers to the table model
            for (String header : csvParser.getHeaderNames()) {
                model.addColumn(header);
            }

            // Lesen der Daten aus der CSV-Datei und Hinzufügen der Zeilen zum Tabellenmodell
            // Reading data from the CSV file and adding the rows to the table model
            for (CSVRecord record : csvParser) {
                Object[] rowData = new Object[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    rowData[i] = record.get(i);
                }
                model.addRow(rowData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Überprüfen, ob eine Datei existiert
    // Method for checking if a file exists
    public static boolean fileExists(String csvFilePath) {
        return new File(csvFilePath).exists();
    }

    // Methode zum Speichern von Daten aus einem Tabellenmodell in eine CSV-Datei
    // Method for saving data from a table model to a CSV file
    public static void saveData(String csvFilePath, DefaultTableModel model) {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (i > 0) {
                    writer.append(';');
                }
                writer.append(escapeSpecialCharacters(model.getColumnName(i))); // Schreiben der Spaltenüberschriften
            }                                                                   // Writing the column headers
            writer.append('\n');

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (j > 0) {
                        writer.append(';');
                    }
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        writer.append(escapeSpecialCharacters(value.toString())); // Schreiben der Daten aus dem Tabellenmodell
                    }                                                             // Writing the data from the table model
                }
                writer.append('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Methode zum Escapen von Sonderzeichen für die Verwendung in CSV-Dateien
    // Method for escaping special characters for use in CSV files
    private static String escapeSpecialCharacters(String input) {
        if (input.contains(";") || input.contains("\"") || input.contains("\n")) {
            return "\"" + input.replaceAll("\"", "\"\"") + "\"";
        }
        return input;
    }
}