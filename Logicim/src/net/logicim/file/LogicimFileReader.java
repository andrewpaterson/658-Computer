package net.logicim.file;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.SaveData;
import net.logicim.data.SaveDataClassStore;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.common.ArrayData;
import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.ui.common.Rotation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.data.ReflectiveData.LOGICIM_TAG_NAME;

public class LogicimFileReader
    extends DefaultHandler
{
  protected CircuitData circuitData;
  protected List<SaveData> dataStack;

  public LogicimFileReader()
  {
    circuitData = null;
  }

  public CircuitData load(File file)
  {
    try
    {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      SAXParser parser = saxParserFactory.newSAXParser();
      parser.parse(new FileInputStream(file), this);
    }
    catch (ParserConfigurationException | SAXException | IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    return circuitData;
  }

  @Override
  public void startDocument()
  {
    circuitData = new CircuitData();
  }

  @Override
  public void startElement(String uri, String lName, String qName, Attributes attr)
  {
    if (qName.equals(LOGICIM_TAG_NAME))
    {
      dataStack = new ArrayList<>();
    }
    else
    {
      String type = getType(attr);
      if (type != null)
      {
        SaveData o = createDataObject(type);
        if (o != null)
        {
          dataStack.add(o);
        }
        else
        {
          throw new SimulatorException("Cannot create Data Object of Type [%s].", type);
        }
      }
    }
    StringBuilder builder = new StringBuilder("Start: " + qName + " ");
    if (attr != null)
    {
      int length = attr.getLength();
      for (int i = 0; i < length; i++)
      {
        String attrQName = attr.getQName(i);
        String attrValue = attr.getValue(i);
        builder.append(attrQName + "=" + attrValue + " ");
      }
    }
    System.out.println(builder.toString());
  }

  protected SaveData createDataObject(String type)
  {
    if (ArrayList.class.getSimpleName().equals(type))
    {
      return new ArrayData();
    }
    else if (Int2D.class.getSimpleName().equals(type))
    {
      return new Int2DData();
    }
    else if (Rotation.class.getSimpleName().equals(type))
    {
      return new RotationData();
    }
    else
    {
      String savedDataClassName = type + "Data";
      Class<SaveData> aClass = SaveDataClassStore.getInstance().getClass(savedDataClassName);
      ClassInspector classInspector = ClassInspector.forClass(aClass);
      return (SaveData) classInspector.newInstance();
    }
  }

  private String getType(Attributes attr)
  {
    int length = attr.getLength();
    for (int i = 0; i < length; i++)
    {
      String attrQName = attr.getQName(i);
      if (attrQName.equals(ReflectiveData.TYPE))
      {
        return attr.getValue(i);
      }
    }
    return null;
  }

  @Override
  public void characters(char[] ch, int start, int length)
  {
    String trim = String.valueOf(ch, start, length).trim();
    if (!trim.isEmpty())
    {
      System.out.println(trim);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  {
    System.out.println("End:   " + qName);
  }
}

