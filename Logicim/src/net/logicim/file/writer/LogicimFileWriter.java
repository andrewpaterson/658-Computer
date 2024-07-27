package net.logicim.file.writer;

import net.common.SimulatorException;
import net.logicim.data.common.SaveData;
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
import java.io.Writer;

import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_DOC_NAME;

public abstract class LogicimFileWriter
{
  public static void writeXML(SaveData editorData, File file)
  {
    FileWriter writer = null;
    try
    {
      writer = new FileWriter(file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    writeXML(editorData, writer, LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME);
  }

  public static void writeXML(SaveData saveData, Writer writer, String docName, String rootTagName)
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement(docName);
      doc.appendChild(root);

      ReflectiveWriter.writeReflectiveData(doc, root, rootTagName, saveData);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      DOMSource source = new DOMSource(doc);

      StreamResult streamResult = new StreamResult(writer);
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(source, streamResult);
      writer.close();
    }
    catch (ParserConfigurationException | TransformerException | IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }
}

