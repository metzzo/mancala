package at.pwd.boardgame.services;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Map;

/**
 * Singleton service for executing XSL Transformations on XML
 */
public class XSLTService {
    private static XSLTService instance;

    private XSLTService() {}

    /**
     * @return Returns the service instance
     */
    public static XSLTService getInstance() {
        if (instance == null) {
            instance = new XSLTService();
        }
        return instance;
    }

    /**
     * Executes XSLT
     *
     * @param xsltPath The path to the XSLT.
     * @param xmlSource The XML that should be transformed
     * @param parameters Additional parameters for the transformer
     * @return the InputStream that contains the generated XML
     */
    public InputStream execute(String xsltPath, StreamSource xmlSource, Map<String, String> parameters) {
        final PipedOutputStream transformOutput = new PipedOutputStream();
        final PipedInputStream fxmlInputStream;
        try {
            fxmlInputStream = new PipedInputStream(transformOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread transformThread = new Thread( () -> {
            try {
                StreamSource xsltSource = new StreamSource(getClass().getResourceAsStream(xsltPath));
                Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
                for (String paramKey : parameters.keySet()) {
                    String paramVal = parameters.get(paramKey);
                    transformer.setParameter(paramKey, paramVal);
                }
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
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

    /**
     * Reads the entire string from an InputStream
     * @param is the stream
     * @return the string containing the content of is
     */
    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
