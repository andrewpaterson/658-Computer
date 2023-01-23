package net.logicim.file.reader;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.*;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.common.*;
import net.logicim.data.field.ProcessedXMLDataField;
import net.logicim.data.field.SaveXMLDataField;
import net.logicim.data.field.UnknownXMLDataField;
import net.logicim.data.field.XMLDataField;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.file.writer.ReflectiveWriter.*;

public class LogicimFileReader
    extends DefaultHandler
{
  protected CircuitData circuitData;
  protected List<XMLDataField> dataStack;

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

  public CircuitData load(String contents)
  {
    try
    {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      SAXParser parser = saxParserFactory.newSAXParser();
      parser.parse(new ByteArrayInputStream(contents.getBytes()), this);
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
      return new ArrayListData();
    }
    else if (Int2D.class.getSimpleName().equals(type))
    {
      return new Int2DData();
    }
    else if (Float2D.class.getSimpleName().equals(type))
    {
      return new Float2DData();
    }
    else if (Rotation.class.getSimpleName().equals(type))
    {
      return new RotationData();
    }
    else if (int[].class.getSimpleName().equals(type))
    {
      return new IntArrayData();
    }
    else if (long[].class.getSimpleName().equals(type))
    {
      return new LongArrayData();
    }
    else if (float[].class.getSimpleName().equals(type))
    {
      return new FloatArrayData();
    }
    else if (double[].class.getSimpleName().equals(type))
    {
      return new DoubleArrayData();
    }
    else if (int[][].class.getSimpleName().equals(type))
    {
      return new IntArray2DData();
    }
    else if (long[][].class.getSimpleName().equals(type))
    {
      return new LongArray2DData();
    }
    else if (float[][].class.getSimpleName().equals(type))
    {
      return new FloatArray2DData();
    }
    else if (double[][].class.getSimpleName().equals(type))
    {
      return new DoubleArray2DData();
    }
    else if (Enum.class.getSimpleName().equals(type))
    {
      return new EnumData();
    }
    else
    {
      String savedDataClassName = type;
      if (!type.endsWith("State"))
      {
        savedDataClassName = type + "Data";
      }
      Class aClass = SaveDataClassStore.getInstance().getClass(savedDataClassName);
      if (aClass == null)
      {
        throw new SimulatorException("Cannot instantiate class [%s].", savedDataClassName);
      }
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
        XMLDataField xmlDataField = dataStack.get(dataStack.size() - 1);
        SaveXMLDataField containingClassField = (SaveXMLDataField) dataStack.get(dataStack.size() - 2);
        InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);
        boolean processed = true;
        if (xmlDataField instanceof UnknownXMLDataField)
        {
          UnknownXMLDataField unknownDataField = (UnknownXMLDataField) xmlDataField;
          Object value = parsePrimitive(trim, unknownDataField, containingClassField, instanceInspector);
          setPrimitiveFieldValue(unknownDataField, containingClassField, instanceInspector, value);
        }
        else if (xmlDataField instanceof SaveXMLDataField)
        {
          processed = false;
          SaveXMLDataField saveXMLDataField = (SaveXMLDataField) xmlDataField;
          if (saveXMLDataField.typeInstance instanceof IntArrayData)
          {
            parseIntArray((IntArrayData) saveXMLDataField.typeInstance, trim);
          }
          else if (saveXMLDataField.typeInstance instanceof LongArrayData)
          {
            parseLongArray((LongArrayData) saveXMLDataField.typeInstance, trim);
          }
          else if (saveXMLDataField.typeInstance instanceof FloatArrayData)
          {
            parseFloatArray((FloatArrayData) saveXMLDataField.typeInstance, trim);
          }
          else if (saveXMLDataField.typeInstance instanceof DoubleArrayData)
          {
            parseDoubleArray((DoubleArrayData) saveXMLDataField.typeInstance, trim);
          }
        }
        if (processed)
        {
          dataStack.remove(dataStack.size() - 1);
          dataStack.add(new ProcessedXMLDataField(xmlDataField.fieldName, xmlDataField.attributes));
        }
      }
    }
  }

  private boolean parseIntArray(IntArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        int i = Integer.parseInt(s);
        arrayData.array[arrayData.index] = i;
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean parseLongArray(LongArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        long i = Long.parseLong(s);
        arrayData.array[arrayData.index] = i;
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean parseFloatArray(FloatArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        float f = Float.parseFloat(s);
        arrayData.array[arrayData.index] = f;
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean parseDoubleArray(DoubleArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        double f = Double.parseDouble(s);
        arrayData.array[arrayData.index] = f;
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  protected void setPrimitiveFieldValue(UnknownXMLDataField unknownDataField, SaveXMLDataField containingClassField, InstanceInspector instanceInspector, Object value)
  {
    try
    {
      instanceInspector.setFieldValue(unknownDataField.fieldName, value);
    }
    catch (IllegalArgumentException e)
    {
      throw new SimulatorException("Cannot set field [%s.%s] to [(%s)%S].",
                                   containingClassField.typeInstance.getClass().getSimpleName(),
                                   unknownDataField.fieldName,
                                   instanceInspector.getFieldClass(unknownDataField.fieldName).getSimpleName(),
                                   value);
    }
  }

  protected Object parsePrimitive(String characters, UnknownXMLDataField unknownDataField, SaveXMLDataField containingClassField, InstanceInspector instanceInspector)
  {
    Class<?> fieldClass = instanceInspector.getFieldClass(unknownDataField.fieldName);

    Object value = null;
    if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class))
    {
      value = Integer.parseInt(characters);
    }
    else if (fieldClass.equals(Boolean.class) || fieldClass.equals(boolean.class))
    {
      value = Boolean.parseBoolean(characters);
    }
    else if (fieldClass.equals(Long.class) || fieldClass.equals(long.class))
    {
      value = Long.parseLong(characters);
    }
    else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class))
    {
      value = Float.parseFloat(characters);
    }
    else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class))
    {
      value = Double.parseDouble(characters);
    }
    else if (fieldClass.equals(Character.class) || fieldClass.equals(char.class))
    {
      value = characters.charAt(0);
    }
    else if (fieldClass.equals(String.class))
    {
      value = characters;
    }
    return value;
  }

  private void startReflectiveData(ReflectiveData reflectiveData, Map<String, String> attributes)
  {
  }

  private void startState(State state, Map<String, String> attributes)
  {
  }

  private void startObjectData(ObjectData objectData, Map<String, String> attributes)
  {
    objectData.load(attributes);
  }

  private void startArrayData(ArrayData arrayData, Map<String, String> attributes)
  {
    arrayData.load(attributes);
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
      Map<String, String> attributeMap = getAttributes(attr);
      String type = attributeMap.get(TYPE);
      if (type != null)
      {
        SaveData o = createDataObject(type);
        if (o != null)
        {
          dataStack.add(new SaveXMLDataField(qName, o, attributeMap));
          if (o instanceof ObjectData)
          {
            startObjectData((ObjectData) o, attributeMap);
          }
          else if (o instanceof ArrayData)
          {
            startArrayData((ArrayData) o, attributeMap);
          }
          else if (o instanceof ReflectiveData)
          {
            startReflectiveData((ReflectiveData) o, attributeMap);
          }
          else if (o instanceof State)
          {
            startState((State) o, attributeMap);
          }
        }
        else
        {
          throw new SimulatorException("Cannot create Data Object of Type [%s].", type);
        }
      }
      else
      {
        dataStack.add(new UnknownXMLDataField(qName, attributeMap));
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
        SaveXMLDataField saveDataField = (SaveXMLDataField) dataStack.get(dataStack.size() - 1);
        circuitData = (CircuitData) saveDataField.typeInstance;
      }
      else
      {
        if (dataStack.size() - 2 >= 0)
        {
          XMLDataField xmlDataField = dataStack.get(dataStack.size() - 1);
          Map<String, String> attributes = xmlDataField.attributes;
          SaveXMLDataField containingClassField = (SaveXMLDataField) dataStack.get(dataStack.size() - 2);
          InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);

          if (xmlDataField instanceof SaveXMLDataField)
          {
            SaveXMLDataField saveXMLDataField = (SaveXMLDataField) xmlDataField;
            if (containingClassField.typeInstance instanceof ArrayListData)
            {
              int index = Integer.parseInt(attributes.get(INDEX));
              ArrayListData listData = (ArrayListData) containingClassField.typeInstance;
              listData.list.set(index, saveXMLDataField.typeInstance.getObject());
            }
            else if (containingClassField.typeInstance instanceof IntArray2DData)
            {
              int index = Integer.parseInt(saveXMLDataField.fieldName.replace("array", ""));
              IntArray2DData intArray2DData = (IntArray2DData) containingClassField.typeInstance;
              intArray2DData.array[index] = ((IntArrayData) saveXMLDataField.typeInstance).array;
            }
            else if (containingClassField.typeInstance instanceof LongArray2DData)
            {
              int index = Integer.parseInt(saveXMLDataField.fieldName.replace("array", ""));
              LongArray2DData longArray2DData = (LongArray2DData) containingClassField.typeInstance;
              longArray2DData.array[index] = ((LongArrayData) saveXMLDataField.typeInstance).array;
            }
            else if (containingClassField.typeInstance instanceof FloatArray2DData)
            {
              int index = Integer.parseInt(saveXMLDataField.fieldName.replace("array", ""));
              FloatArray2DData floatArray2DData = (FloatArray2DData) containingClassField.typeInstance;
              floatArray2DData.array[index] = ((FloatArrayData) saveXMLDataField.typeInstance).array;
            }
            else if (containingClassField.typeInstance instanceof DoubleArray2DData)
            {
              int index = Integer.parseInt(saveXMLDataField.fieldName.replace("array", ""));
              DoubleArray2DData doubleArray2DData = (DoubleArray2DData) containingClassField.typeInstance;
              doubleArray2DData.array[index] = ((DoubleArrayData) saveXMLDataField.typeInstance).array;
            }
            else
            {
              instanceInspector.setFieldValue(saveXMLDataField.fieldName, saveXMLDataField.typeInstance.getObject());
            }
          }
          else if (xmlDataField instanceof UnknownXMLDataField)
          {
          }
          else if (xmlDataField instanceof ProcessedXMLDataField)
          {
          }
        }
      }
      dataStack.remove(dataStack.size() - 1);
    }
  }
}

