package main.java.transform;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hasithad on 4/7/14.
 */
public class MyTransformer {

    public static void identityTransform(String fileName,String xslFileName) throws TransformerException, ParserConfigurationException, IOException, SAXException {

        System.out.println("----- DOM to XML Identity Transformation -----");

        Document inputDOM = null;

        DocumentBuilderFactory buildFactory = DocumentBuilderFactory.newInstance();

        //buildFactory.setNamespaceAware(true);
        //buildFactory.setValidating(true);//when using a schema, this property need not be set.
        //refer : http://stackoverflow.com/questions/5622101/validating-against-a-schema-with-documentbuilder

        DocumentBuilder builder = buildFactory.newDocumentBuilder();

        inputDOM = builder.parse(new FileInputStream(fileName));

        TransformerFactory transformFactory = TransformerFactory.newInstance();

        Transformer domTransformer = transformFactory.newTransformer(new StreamSource(xslFileName));

        domTransformer.setOutputProperty(OutputKeys.INDENT,"yes");

        DOMSource input = new DOMSource(inputDOM);

        StreamResult output = new StreamResult(System.out);

        domTransformer.transform(input,output);

        System.out.println("----- End of DOM to XML Identity Transformation -----");

    }
}
