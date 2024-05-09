package xmlWork;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Hashtable;

public class EthnicityHandler extends DefaultHandler {
    private boolean isEthnicity = false;
    private final Hashtable<String, Integer> ethnicityValues = new Hashtable<>();


    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("ethcty")) {
            isEthnicity = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isEthnicity) {
            String ethnicity = new String(ch, start, length);
            if (ethnicityValues.containsKey(ethnicity)) {
                int count = ethnicityValues.get(ethnicity);
                ethnicityValues.put(ethnicity, count + 1);
            } else {
                ethnicityValues.put(ethnicity, 1);
            }
            isEthnicity = false;
        }
    }

    public Hashtable<String, Integer> getEthnicityValues() {
        return ethnicityValues;
    }
}
