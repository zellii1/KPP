package xmlWork;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DataHandler extends DefaultHandler {
    private final StringBuilder currentElement = new StringBuilder();
    private int rows = 0;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("row")) {
            rows++;
            if (rows > 4) {
                throw new SAXException("I guess this is enough");
            }
        }

        currentElement.append(qName);
        currentElement.append(" [");

        for (int i = 0; i < attributes.getLength(); i++) {
            currentElement.append(attributes.getQName(i));
            currentElement.append("=");
            currentElement.append(attributes.getValue(i));
            currentElement.append(",");
        }

        currentElement.append("] ");
    }

    public void endElement(String uri, String localName, String qName) {
        System.out.println(currentElement);
        currentElement.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (!currentElement.toString().equals("row")) {
            String value = new String(ch, start, length).trim();
            if (!value.isEmpty()) {
                currentElement.append(" = ").append(value);
            }
        }
    }
}