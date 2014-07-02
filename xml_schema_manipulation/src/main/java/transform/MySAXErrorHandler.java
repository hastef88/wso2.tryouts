package main.java.transform;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by hasithad on 4/3/14.
 */
public class MySAXErrorHandler implements ErrorHandler{

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }
}
