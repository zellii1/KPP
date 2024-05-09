package org.example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class DataSheetTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private int columnCount;
    private int rowCount;
    private DataSheet dataSheet;
    private final String[] columnNames;

    private final ArrayList<DataSheetChangeListener> listeners = new ArrayList<>();
    private final DataSheetChangeEvent event = new DataSheetChangeEvent(this);

    public DataSheetTableModel() {
        columnCount = 3;
        rowCount = 0;
        try {
            dataSheet = new DataSheet();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        columnNames = new String[]{"Date", "X Value", "Y Value"};
    }

    public DataSheetTableModel(DataSheet dataSheet) {
        this();
        this.dataSheet = dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
        rowCount = dataSheet.getCountOfData();
        fireDataSheetChange();
    }

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 0;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            if (dataSheet != null) {
                switch (columnIndex) {
                    case 0:
                        dataSheet.setDate((String) aValue, rowIndex);
                        fireDataSheetChange();
                        break;
                    case 1:
                        dataSheet.setX(Double.parseDouble((String) aValue), rowIndex);
                        fireDataSheetChange();
                        break;
                    case 2:
                        dataSheet.setY(Double.parseDouble((String) aValue), rowIndex);
                        fireDataSheetChange();
                        break;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Неверное значение", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataSheet == null) return null;
        switch (columnIndex) {
            case 0:
                return dataSheet.getDate(rowIndex);
            case 1:
                return dataSheet.getX(rowIndex);
            case 2:
                return dataSheet.getY(rowIndex);
            default:
                return null;
        }
    }

    public void setRowCount(int rowCount) {
        if (rowCount >= 0) {
            this.rowCount = rowCount;
        }
    }

    public void addDataSheetChangeListener(DataSheetChangeListener listener) {
        listeners.add(listener);
    }

    public void removeDataSheetChangeListener(DataSheetChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireDataSheetChange() {
        for (DataSheetChangeListener listener : listeners) {
            listener.dataChanged(event);
        }
    }

    public void clearDataSheet() {
        int size = dataSheet.getCountOfData();
        for (int i = 0; i < size; i++)
            dataSheet.removeData(0);
        dataSheet.showAllDoc();
        setRowCount(0);
        fireDataSheetChange();
    }
}
