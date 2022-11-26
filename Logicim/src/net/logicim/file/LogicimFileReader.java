package net.logicim.file;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Int2D;
import net.logicim.data.PrimitiveData;
import net.logicim.data.ReflectiveData;
import net.logicim.data.SaveData;
import net.logicim.data.SaveDataClassStore;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.common.ArrayData;
import net.logicim.data.common.Int2DData;
import net.logicim.data.common.RotationData;
import net.logicim.data.field.DataField;
import net.logicim.data.field.TypedDataField;
import net.logicim.data.field.UntypedDataField;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.data.ReflectiveData.*;

public class LogicimFileReader
    extends DefaultHandler
{
  protected CircuitData circuitData;
  protected List<DataField> dataStack;

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

  private Map<String, String> getAttributes(Attributes attr)
  {
    int length = attr.getLength();
    HashMap<String, String> map = new HashMap<>();

    for (int i = 0; i < length; i++)
    {
      String attrQName = attr.getQName(i);
      map.put(attrQName.trim(), attr.getValue(i).trim());
    }
    return map;
  }

  @Override
  public void characters(char[] ch, int start, int length)
  {
    String trim = String.valueOf(ch, start, length).trim();
    if (!trim.isEmpty())
    {
      if (dataStack.size() - 2 >= 0)
      {
        UntypedDataField primitiveField = (UntypedDataField) dataStack.get(dataStack.size() - 1);
        TypedDataField containingClassField = (TypedDataField) dataStack.get(dataStack.size() - 2);
        InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);
        Class<?> fieldClass = instanceInspector.getFieldClass(primitiveField.fieldName);

        Object value = null;
        if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class))
        {
          value = Integer.parseInt(trim);
        }
        else if (fieldClass.equals(Boolean.class) || fieldClass.equals(boolean.class))
        {
          value = Boolean.parseBoolean(trim);
        }
        else if (fieldClass.equals(Long.class) || fieldClass.equals(long.class))
        {
          value = Long.parseLong(trim);
        }
        else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class))
        {
          value = Float.parseFloat(trim);
        }
        else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class))
        {
          value = Double.parseDouble(trim);
        }
        try
        {
          instanceInspector.setFieldValue(primitiveField.fieldName, value);
        }
        catch (IllegalArgumentException e)
        {
          throw new SimulatorException("Cannot set field [%s.%s] to [(%s)%S].",
                                       containingClassField.typeInstance.getClass().getSimpleName(),
                                       primitiveField.fieldName,
                                       instanceInspector.getFieldClass(primitiveField.fieldName).getSimpleName(),
                                       value);
        }
      }
    }
  }

  private void startReflectiveData(ReflectiveData reflectiveData, Map<String, String> attributes)
  {
    if (dataStack.size() - 2 >= 0)
    {
      TypedDataField field = (TypedDataField) dataStack.get(dataStack.size() - 1);
      TypedDataField containingClassField = (TypedDataField) dataStack.get(dataStack.size() - 2);
      InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);
      if (containingClassField.typeInstance instanceof ArrayData)
      {
        int index = Integer.parseInt(attributes.get(INDEX));
        ArrayData arrayData = (ArrayData) containingClassField.typeInstance;
        arrayData.list.set(index, field.typeInstance.getObject());
      }
      else
      {
        instanceInspector.setFieldValue(field.fieldName, field.typeInstance.getObject());
      }
    }
  }

  private void startPrimitiveData(PrimitiveData primitiveData, Map<String, String> attributes)
  {
    primitiveData.load(attributes);

    if (dataStack.size() - 2 >= 0)
    {
      TypedDataField field = (TypedDataField) dataStack.get(dataStack.size() - 1);
      TypedDataField containingClassField = (TypedDataField) dataStack.get(dataStack.size() - 2);
      InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);
      instanceInspector.setFieldValue(field.fieldName, field.typeInstance.getObject());
    }
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
      Map<String, String> attributes = getAttributes(attr);
      String type = attributes.get(ReflectiveData.TYPE);
      if (type != null)
      {
        SaveData o = createDataObject(type);
        if (o != null)
        {
          dataStack.add(new TypedDataField(qName, o));
          if (o instanceof PrimitiveData)
          {
            startPrimitiveData((PrimitiveData) o, attributes);
          }
          else if (o instanceof ReflectiveData)
          {
            startReflectiveData((ReflectiveData) o, attributes);
          }
        }
        else
        {
          throw new SimulatorException("Cannot create Data Object of Type [%s].", type);
        }
      }
      else
      {
        dataStack.add(new UntypedDataField(qName));
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  {
    if (qName.equals(LOGICIM_TAG_NAME))
    {
      dataStack = null;
    }
    else
    {
      if (qName.equals(CIRCUIT_DATA_TAG_NAME))
      {
        TypedDataField typedDataField = (TypedDataField) dataStack.get(dataStack.size() - 1);
        circuitData = (CircuitData) typedDataField.typeInstance;
      }
      dataStack.remove(dataStack.size() - 1);
    }
  }
}

