package org.example;

import javax.swing.*;
import java.awt.*;

public class DataSheetTablePanel extends JPanel {

    private final DataSheetTable dataSheetTable;

    public DataSheetTablePanel() {
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        JButton addButton;
        buttonPanel.add(addButton = new JButton("Add Point"));
        JButton delButton;
        buttonPanel.add(delButton = new JButton("Delete Point"));
        this.add(buttonPanel, BorderLayout.SOUTH);

        dataSheetTable = new DataSheetTable(new DataSheetTableModel());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(dataSheetTable);
        this.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            dataSheetTable.getModel().setRowCount(dataSheetTable.getModel().getRowCount() + 1);
            dataSheetTable.getModel().getDataSheet().addData(dataSheetTable.getModel().getDataSheet().createNewElement());
            dataSheetTable.revalidate();
            dataSheetTable.getModel().fireDataSheetChange();
        });

        delButton.addActionListener(e -> {
            dataSheetTable.getModel().getDataSheet().removeData(dataSheetTable.getModel().getRowCount() - 1);
            dataSheetTable.getModel().setRowCount(dataSheetTable.getModel().getRowCount() - 1);
            dataSheetTable.revalidate();
            dataSheetTable.getModel().fireDataSheetChange();
        });
    }

    public DataSheetTable getDataSheetTable() {
        return dataSheetTable;
    }
}
