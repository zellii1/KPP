package xmlWork;

import org.xml.sax.SAXException;
import xmlWork.DataHandler;
import xmlWork.EthnicityHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class Main {
    static final String FILE_PATH = "src/resources/Popular_Baby_Names_NY.xml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // SAX example
        SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = SAXFactory.newSAXParser();
        DataHandler handler = new DataHandler();

        try {
            saxParser.parse(new File(FILE_PATH), handler);
        } catch (SAXException e) {
            if (e.getMessage().equals("I guess this is enough")) {
                System.out.println("Done parsing!");
            }
        }

        System.out.println("\t--- --- ---");
        // Validating
        XMLValidator validator = new XMLValidator("src/resources/Popular_Baby_Names_NY.xsd");
        boolean isValid = validator.validate(FILE_PATH);

        if (isValid) {
            System.out.println("Congrats! The XML file is valid! 🎉");
        } else {
            System.out.println(":( The XML file is invalid!");
        }

        System.out.println("\t--- --- ---");

        // Extract ethnicity
        EthnicityHandler ethnicityHandler = new EthnicityHandler();
        saxParser.parse(FILE_PATH, ethnicityHandler);
        Hashtable<String, Integer> ethnicityValues = ethnicityHandler.getEthnicityValues();

        for (String ethnicity : ethnicityValues.keySet()) {
            int count = ethnicityValues.get(ethnicity);
            System.out.println("Ethnicity: " + ethnicity + ", Count: " + count);
        }
    }
}