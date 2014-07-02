package main.java.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by hasithad on 4/3/14.
 */
public class MySAXHandler extends DefaultHandler{

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        System.out.println("xml node found : " + localName);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {

        e.printStackTrace();
    }
}
