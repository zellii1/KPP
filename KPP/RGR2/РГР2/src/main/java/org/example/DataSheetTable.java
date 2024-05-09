package org.example;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class DataSheetTable extends JTable {

    private final ArrayList<DataSheetChangeListener> listeners = new ArrayList<>();

    public DataSheetTable(TableModel dm) {
        super(dm);
    }

    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
    }

    public DataSheetTableModel getModel() {
        return (DataSheetTableModel) super.getModel();
    }

    public ArrayList<DataSheetChangeListener> getListeners() {
        return listeners;
    }
}
