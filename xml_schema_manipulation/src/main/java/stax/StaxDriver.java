package main.java.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by hasithad on 4/4/14.
 */
public class StaxDriver {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String BOOK_SCHEMA = "src/main/resources/books.xsd";

    public static void parse(String filePath) throws XMLStreamException, FileNotFoundException {

        System.out.println("----- Stax Parser Example -----");

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_COALESCING,true);

        XMLStreamReader reader = null;

        reader = inputFactory.createXMLStreamReader(new FileInputStream(filePath));

        int event = reader.getEventType();

        StringBuilder parseResult = new StringBuilder();

        while(true) {

            switch(event) {

                case XMLStreamConstants.START_ELEMENT:
                    parseResult.append("<" + reader.getName());
                    for(int i = 0, n = reader.getAttributeCount(); i < n; ++i)
                        parseResult.append(" " + reader.getAttributeName(i) + "\""
                                + "=\"" + reader.getAttributeValue(i) + "\" ");
                    parseResult.append(">");
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    parseResult.append("</" + reader.getName() + ">");
                    break;
                case XMLStreamConstants.CHARACTERS:
                    parseResult.append(reader.getText());
                    break;
                default:
                    break;
            }

            if (reader.hasNext()) {
                event = reader.next();
            }
            else {
                break;
            }

        }

        reader.close();
        System.out.printf(parseResult.toString());

        System.out.println("----- End of STAX Parser Example -----");

    }
}
