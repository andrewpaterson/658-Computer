package net.logicim.file.writer;

import net.logicim.common.SimulatorException;
import net.logicim.data.editor.EditorData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_TAG_NAME;

public class LogicimFileWriter
{
  public void writeXML(EditorData editorData, File file)
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement(LOGICIM_TAG_NAME);
      doc.appendChild(root);

      ReflectiveWriter.writeXML(editorData, doc, EDITOR_DATA_TAG_NAME, root);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      DOMSource source = new DOMSource(doc);

      FileWriter writer = new FileWriter(file);
      StreamResult streamResult = new StreamResult(writer);
      transformer.transform(source, streamResult);
      writer.close();
    }
    catch (ParserConfigurationException | TransformerException | IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }
}

