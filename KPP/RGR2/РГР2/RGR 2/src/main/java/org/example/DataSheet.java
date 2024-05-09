package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataSheet {

    private Document document;

    public DataSheet() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        document = docBuilder.newDocument();
        document.appendChild(document.createElement("DataSheet"));
    }

    public DataSheet(Document document) {
        this.document = document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public void showAllDoc() {
        System.out.println("Root element " + document.getDocumentElement().getTagName());
        for (Node child = document.getDocumentElement().getFirstChild(); child != null; child = child.getNextSibling()) {
            System.out.println(child.getNodeName() + " " + child.getAttributes().item(0).getNodeValue());
            System.out.println(child.getChildNodes().item(0).getNodeName() + " " + child.getChildNodes().item(0).getTextContent());
            System.out.println(child.getChildNodes().item(1).getNodeName() + " " + child.getChildNodes().item(1).getTextContent());
        }
    }

    public int getCountOfData() {
        return document.getElementsByTagName("data").getLength();
    }

    public String getX(int pos) {
        return document.getElementsByTagName("x").item(pos).getTextContent();
    }

    public void setX(double val, int pos) {
        document.getElementsByTagName("x").item(pos).setTextContent(String.valueOf(val));
    }

    public String getY(int pos) {
        return document.getElementsByTagName("y").item(pos).getTextContent();
    }

    public void setY(double val, int pos) {
        document.getElementsByTagName("y").item(pos).setTextContent(String.valueOf(val));
    }

    public String getDate(int pos) {
        return document.getElementsByTagName("data").item(pos).getAttributes().item(0).getTextContent();
    }

    public void setDate(String date, int pos) {
        document.getElementsByTagName("data").item(pos).getAttributes().item(0).setTextContent(String.valueOf(date));
    }

    public Element createNewElement() {
        Element data = document.createElement("data");
        data.setAttribute("date", getCurrentDate());

        Element tmp = document.createElement("x");
        tmp.appendChild(document.createTextNode("0.0"));
        data.appendChild(tmp);

        tmp = document.createElement("y");
        tmp.appendChild(document.createTextNode("0.0"));
        data.appendChild(tmp);
        return data;
    }

    public void addData(Element data) {
        document.getDocumentElement().appendChild(data);
    }

    public void removeData(int pos) {
        if (pos >= 0)
            document.getDocumentElement().removeChild(document.getElementsByTagName("data").item(pos));
    }

    public void saveDocument(File file) throws TransformerException, FileNotFoundException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1251");
        DOMSource xmlSource = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(file));
        transformer.transform(xmlSource, result);
    }

    public void fillDocument(File xmlFile) throws TransformerException, FileNotFoundException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);

        Schema schema = null;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            schema = schemaFactory.newSchema(new File("data.xsd"));
        } catch (SAXException e) {
            e.printStackTrace();
        }
        dbf.setSchema(schema);

        MyErrorHandler myErrorHandler = new MyErrorHandler();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(myErrorHandler);
            this.document = db.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
