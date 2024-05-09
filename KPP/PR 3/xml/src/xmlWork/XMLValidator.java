package xmlWork;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XMLValidator {
    private final String xsdPath;

    public XMLValidator(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public boolean validate(String xmlData) {

        try {
            // Create SchemaFactory and load the XSD schema
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = new File(xsdFilePath);
            Schema schema = schemaFactory.newSchema(schemaFile);

            // Create Validator and set the ErrorHandler
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new MyErrorHandler());

            // Perform validation
            File xmlFile = new File(xmlFilePath);
            validator.validate(new StreamSource(xmlFile));

            System.out.println("XML document is valid.");
        } catch (Exception e) {
            System.out.println("XML document is not valid: " + e.getMessage());
        }

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlData);
            validator.validate(source);
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
