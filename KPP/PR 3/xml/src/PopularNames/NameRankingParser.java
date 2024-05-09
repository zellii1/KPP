package PopularNames;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class NameRankingParser extends DefaultHandler {
    private static final HashMap<String, Integer> nameCount = new HashMap<>(); // Для сортування
    private static List<Name> names = new ArrayList<>(); // Все що спарсили
    private final StringBuilder data = new StringBuilder(); // Для парсингу значень в xml
    private Name currentName;

    public static void main(String[] args) {
        int target = 5;
        String ethnicity = "HISPANIC";

        NameRankingParser parser = new NameRankingParser();
        parser.parse();

        List<Name> filteredNames = new ArrayList<>();

        // Цього можна униктнути, але вже що є
        for (Name name : names) {
            if (name.getEthnicity().equals(ethnicity)) {
                nameCount.put(name.getName(), nameCount.getOrDefault(name.getName(), 0) + 1);
                filteredNames.add(name); // Беремо тільки якщо ethnicity співпадає
            }
        }

        names = filteredNames;

        StringBuilder mostPopularNames = new StringBuilder();
        for (int i = 0; i < target; i++) {
            String popularName = "";
            int maxCount = 0;

            for (Map.Entry<String, Integer> entry : nameCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    popularName = entry.getKey();
                }
            }

            mostPopularNames.append(popularName).append(" - ").append(maxCount).append("\n");
            nameCount.remove(popularName);
        }

        System.out.println("The most popular names:");
        System.out.println(mostPopularNames);
        System.out.println("--- --- ---");

        // Сортуємо щоб отримати найпопулярніші names
        names.sort(Comparator.comparingInt(name -> nameCount.getOrDefault(name.getName(), 0)));
        names = filteredNames.subList(0, target); // Більше ніж target нам не треба

        // Сортуємо по rank
        names.sort(Comparator.comparingInt(Name::getRanking).reversed());

        System.out.println(names);
        System.out.println("--- --- ---");
        saveDocument();
        System.out.println("--- --- ---");
        readDocument();

    }

    private static void readDocument() {
        try {
            File inputFile = new File("src/resources/Sorted_Names.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            Element root = document.getDocumentElement();

            NodeList rowList = root.getElementsByTagName("row");

            for (int i = 0; i < rowList.getLength(); i++) {
                Element rowElement = (Element) rowList.item(i);

                String name = rowElement.getElementsByTagName("nm").item(0).getTextContent();
                String gender = rowElement.getElementsByTagName("gndr").item(0).getTextContent();
                int count = Integer.parseInt(rowElement.getElementsByTagName("cnt").item(0).getTextContent());
                int ranking = Integer.parseInt(rowElement.getElementsByTagName("rnk").item(0).getTextContent());

                // Process the extracted data as needed
                System.out.println("Name: " + name);
                System.out.println("Gender: " + gender);
                System.out.println("Count: " + count);
                System.out.println("Ranking: " + ranking);
                System.out.println("-----------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("row");
            document.appendChild(rootElement);

            for (Name name : names) {
                Element rowElement = document.createElement("row");

                Element nameElement = document.createElement("nm");
                Text nameValue = document.createTextNode(name.getName());
                nameElement.appendChild(nameValue);
                rowElement.appendChild(nameElement);

                Element countElement = document.createElement("cnt");
                Text countValue = document.createTextNode(String.valueOf(name.getCount()));
                countElement.appendChild(countValue);
                rowElement.appendChild(countElement);

                Element genderElement = document.createElement("gndr");
                Text genderValue = document.createTextNode(String.valueOf(name.getGender()));
                genderElement.appendChild(genderValue);
                rowElement.appendChild(genderElement);

                Element rankElement = document.createElement("rnk");
                Text rankValue = document.createTextNode(String.valueOf(name.getRanking()));
                rankElement.appendChild(rankValue);
                rowElement.appendChild(rankElement);

                rootElement.appendChild(rowElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("src/resources/Sorted_Names.xml"));
            transformer.transform(source, result);
            System.out.println("XML file saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("row") && attributes.getValue("_id") != null) {
            currentName = new Na();
            names.add(currentName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("nm")) {
            currentName.setName(data.toString().trim());
        } else if (qName.equals("gndr")) {
            currentName.setGender(data.toString().trim());
        } else if (qName.equals("cnt")) {
            currentName.setCount(Integer.parseInt(data.toString().trim()));
        } else if (qName.equals("rnk")) {
            currentName.setRanking(Integer.parseInt(data.toString().trim()));
        } else if (qName.equals("ethcty")) {
            currentName.setEthnicity(data.toString().trim());
        }

        data.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        data.append(ch, start, length);
    }

    private void parse() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse("src/resources/Popular_Baby_Names_NY.xml", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
