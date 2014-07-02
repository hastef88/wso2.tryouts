package main.java.sax;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by hasithad on 4/3/14.
 */
public class SAXDriver {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String BOOK_SCHEMA = "src/main/resources/books.xsd";

    public static void parse(String filePath) throws ParserConfigurationException, SAXException, IOException {

        System.out.println("----- SAX Parser Example -----");

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();

        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);

        SAXParser parser = null;

        parser = parserFactory.newSAXParser();
        parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_SCHEMA);
        parser.setProperty(JAXP_SCHEMA_SOURCE,new File(BOOK_SCHEMA));

        parser.parse(new File(filePath), new MySAXHandler());

        System.out.println("----- End of SAX Parser Example -----");

    }
}
