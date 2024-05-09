package org.example;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

public class Window extends JFrame {

    private final DataSheetTablePanel dataSheetTablePanel;

    private final DataSheetGraph dataSheetGraph;

    private final JFileChooser fileChooser;

    public static void main(String[] args) {
        Window window = new Window();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }

    public Window() {
        setTitle("Laboratory №5");
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataSheetTablePanel = new DataSheetTablePanel();
        this.getContentPane().add(dataSheetTablePanel, BorderLayout.WEST);

        dataSheetGraph = new DataSheetGraph();
        this.add(dataSheetGraph, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        JButton openButton;
        buttonPanel.add(openButton = new JButton("Open"));
        JButton saveButton;
        buttonPanel.add(saveButton = new JButton("Save"));
        JButton clearButton;
        buttonPanel.add(clearButton = new JButton("Clear"));
        JButton exitButton;
        buttonPanel.add(exitButton = new JButton("Close"));
        this.add(buttonPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser(new File("output"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));

        openButton.addActionListener(e -> {
            fileChooser.setDialogTitle("Selected file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showDialog(null, "Selected") == JFileChooser.APPROVE_OPTION)
                try {
                    DataSheet dataSheet = new DataSheet();
                    dataSheet.fillDocument(fileChooser.getSelectedFile());
                    dataSheetTablePanel.getDataSheetTable().getModel().setDataSheet(dataSheet);
                    dataSheetTablePanel.getDataSheetTable().revalidate();
                } catch (ParserConfigurationException | FileNotFoundException | TransformerException e1) {
                    e1.printStackTrace();
                }
        });

        saveButton.addActionListener(e -> {
            fileChooser.setDialogTitle("Save file");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION)
                try {
                    dataSheetTablePanel.getDataSheetTable()
                            .getModel()
                            .getDataSheet()
                            .saveDocument(new File(fileChooser.getSelectedFile(), getLastPathName()));
                } catch (FileNotFoundException | TransformerException e1) {
                    e1.printStackTrace();
                }
        });

        clearButton.addActionListener(e -> {
            dataSheetTablePanel.getDataSheetTable().getModel().clearDataSheet();
            dataSheetTablePanel.getDataSheetTable().revalidate();
        });

        exitButton.addActionListener(e -> System.exit(0));

        dataSheetTablePanel.getDataSheetTable().getModel().addDataSheetChangeListener(e -> {
            dataSheetGraph.setDataSheet(dataSheetTablePanel.getDataSheetTable().getModel().getDataSheet());
            dataSheetGraph.repaint();
        });
    }

    private String getLastPathName() {

        Collection<File> files = FileUtils.listFiles(
                new File("output"), new String[]{"xml"}, true);

        String name;
        if (files.isEmpty()) {
            name = "file_1.xml";
        } else {
            name = replaceNumberWithOneMore(getLastElement(files).getName());
        }

        return name;
    }

    private <T> T getLastElement(final Iterable<T> elements) {
        T lastElement = null;

        for (T element : elements) {
            lastElement = element;
        }

        return lastElement;
    }

    private String replaceNumberWithOneMore(String input) {
        int startIndex = -1;
        int endIndex;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                startIndex = i;
                break;
            }
        }

        if (startIndex == -1) {
            return input;
        }

        endIndex = startIndex;
        while (endIndex < input.length() && Character.isDigit(input.charAt(endIndex))) {
            endIndex++;
        }

        int number = Integer.parseInt(input.substring(startIndex, endIndex));
        String replacement = Integer.toString(number + 1);

        return input.substring(0, startIndex) + replacement + input.substring(endIndex);
    }
}
