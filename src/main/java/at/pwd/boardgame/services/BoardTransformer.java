package at.pwd.boardgame.services;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by rfischer on 13/04/2017.
 */
public class BoardTransformer {
    private static BoardTransformer ourInstance = new BoardTransformer();

    public static BoardTransformer getInstance() {
        return ourInstance;
    }

    private BoardTransformer() {
    }

    public PipedInputStream transform(String inFilename, String xslFilename) {
        final PipedOutputStream transformOutput = new PipedOutputStream();
        final PipedInputStream fxmlInputStream;
        try {
            fxmlInputStream = new PipedInputStream(transformOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread transformThread = new Thread( () -> {
            try {
                StreamSource xsltSource = new StreamSource(getClass().getResourceAsStream(xslFilename));
                Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamSource xmlSource = new StreamSource(getClass().getResourceAsStream(inFilename));
                StreamResult transformerResult = new StreamResult(transformOutput);
                transformer.transform(xmlSource, transformerResult);
                transformOutput.close();

            } catch (IOException | TransformerConfigurationException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                // An error occurred while applying the XSL file
                // Get location of error in input file
                SourceLocator locator = e.getLocator();
                int col = locator.getColumnNumber();
                int line = locator.getLineNumber();
                String publicId = locator.getPublicId();
                String systemId = locator.getSystemId();

                System.out.println(
                        "Error applying XSL in line " + line +
                                " at " + col +
                                " publicId: " + publicId +
                                " systemId: " + systemId
                );
            }
        });
        transformThread.start();
        return fxmlInputStream;
    }
}
