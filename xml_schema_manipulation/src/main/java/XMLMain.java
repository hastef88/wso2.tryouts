package main.java;

import main.java.sax.SAXDriver;
import main.java.stax.StaxDriver;
import main.java.transform.MyTransformer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hasithad on 4/3/14.
 */
public class XMLMain {


    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Please provide a valid filename and a stylesheet file name.");
            System.exit(1);
        }

        try {
            SAXDriver.parse(args[0]);
            StaxDriver.parse(args[0]);
            MyTransformer.identityTransform(args[0],args[1]);

        } catch (SAXException e) {
            System.out.printf("Error during Parsing with SAX Parser");
            e.printStackTrace();

        } catch(TransformerException e) {
            System.out.printf("Error During Transformation");
            e.printStackTrace();

        } catch(IOException e) {
            System.out.printf("Could not locate or get access to the file.");
            e.printStackTrace();

        } catch (ParserConfigurationException e) {
            System.out.printf("Error with ParserFactory configurations.");
            e.printStackTrace();

        } catch (XMLStreamException e) {
            System.out.println("Error occurred during Parsing.");
            e.printStackTrace();
        }
    }
}
