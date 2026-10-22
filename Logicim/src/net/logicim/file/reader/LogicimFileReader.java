package net.logicim.file.reader;

import net.common.SimulatorException;
import net.common.reflect.ClassInspector;
import net.common.reflect.InstanceInspector;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.util.StringUtil;
import net.logicim.data.SaveDataClassStore;
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
import java.util.*;

import static net.logicim.file.writer.ReflectiveWriter.TYPE;

public class LogicimFileReader
    extends DefaultHandler
{
  protected SaveData circuitData;
  protected List<XMLDataField> dataStack;

  protected List<String> characters;
  private String docName;
  private String rootTagName;

  public LogicimFileReader(String docName, String rootTagName)
  {
    this.circuitData = null;
    this.characters = null;
    this.docName = docName;
    this.rootTagName = rootTagName;
  }

  public SaveData load(File file)
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

  public SaveData load(String contents)
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
    else if (KeyData.class.getSimpleName().equals(type))
    {
      return new KeyData();
    }
    else if (MapElementData.class.getSimpleName().equals(type))
    {
      return new MapElementData();
    }
    else if (SetElementData.class.getSimpleName().equals(type))
    {
      return new SetElementData();
    }
    else if (ListElementData.class.getSimpleName().equals(type))
    {
      return new ListElementData();
    }
    else if (ValueData.class.getSimpleName().equals(type))
    {
      return new ValueData();
    }
    else if (LinkedHashMap.class.getSimpleName().equals(type))
    {
      return new LinkedHashMapData();
    }
    else if (HashMap.class.getSimpleName().equals(type))
    {
      return new HashMapData();
    }
    else if (LinkedHashSet.class.getSimpleName().equals(type))
    {
      return new LinkedHashSetData();
    }
    else if (HashSet.class.getSimpleName().equals(type))
    {
      return new HashSetData();
    }
    else if (long.class.getSimpleName().equals(type) || Long.class.getSimpleName().equals(type))
    {
      return new LongData();
    }
    else if (int.class.getSimpleName().equals(type) || Integer.class.getSimpleName().equals(type))
    {
      return new IntData();
    }
    else if (float.class.getSimpleName().equals(type) || Float.class.getSimpleName().equals(type))
    {
      return new FloatData();
    }
    else if (double.class.getSimpleName().equals(type) || Double.class.getSimpleName().equals(type))
    {
      return new DoubleData();
    }
    else
    {
      Class<?> aClass = SaveDataClassStore.getInstance().getClass(type);
      if (aClass == null)
      {
        throw new SimulatorException("Cannot instantiate class [%s].", type);
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
    if (characters == null)
    {
      characters = new ArrayList<>();
    }
    String trim = StringUtil.stripSurroundingWhitespace(String.valueOf(ch, start, length));
    if (!trim.isEmpty())
    {
      characters.add(trim);
    }
  }

  protected void processCharacters(String string)
  {
    if (dataStack.size() - 2 >= 0)
    {
      XMLDataField xmlDataFieldStackMinusOne = dataStack.get(dataStack.size() - 1);
      XMLDataField xmlDataFieldStackMinusTwo = dataStack.get(dataStack.size() - 2);
      if (xmlDataFieldStackMinusTwo instanceof SaveXMLDataField)
      {
        SaveXMLDataField containingClassField = (SaveXMLDataField) xmlDataFieldStackMinusTwo;
        InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);
        boolean processed = true;
        if (xmlDataFieldStackMinusOne instanceof UnknownXMLDataField)
        {
          UnknownXMLDataField unknownDataField = (UnknownXMLDataField) xmlDataFieldStackMinusOne;
          Object value = parsePrimitive(string.trim(), unknownDataField, containingClassField, instanceInspector);
          setPrimitiveFieldValue(unknownDataField, containingClassField, instanceInspector, value);
        }
        else if (xmlDataFieldStackMinusOne instanceof SaveXMLDataField)
        {
          processed = false;
          SaveXMLDataField saveXMLDataField = (SaveXMLDataField) xmlDataFieldStackMinusOne;
          if (saveXMLDataField.typeInstance instanceof IntArrayData)
          {
            parseIntArray((IntArrayData) saveXMLDataField.typeInstance, string);
          }
          else if (saveXMLDataField.typeInstance instanceof LongArrayData)
          {
            parseLongArray((LongArrayData) saveXMLDataField.typeInstance, string);
          }
          else if (saveXMLDataField.typeInstance instanceof FloatArrayData)
          {
            parseFloatArray((FloatArrayData) saveXMLDataField.typeInstance, string);
          }
          else if (saveXMLDataField.typeInstance instanceof DoubleArrayData)
          {
            parseDoubleArray((DoubleArrayData) saveXMLDataField.typeInstance, string);
          }
        }
        if (processed)
        {
          dataStack.remove(dataStack.size() - 1);
          dataStack.add(new ProcessedXMLDataField(xmlDataFieldStackMinusOne.fieldName, xmlDataFieldStackMinusOne.attributes));
        }
      }
      else
      {
        throw new SimulatorException("Found class [%s] on stack.  Expected only [SaveXMLDataField].", xmlDataFieldStackMinusTwo.getClass().getSimpleName());
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
        arrayData.array[arrayData.index] = parseInt(s);
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private int parseInt(String s)
  {
    int value;
    try
    {
      value = Integer.parseInt(s);
    }
    catch (NumberFormatException e)
    {
      throw new SimulatorException("Could not parse [%s] as int.", s);
    }
    return value;
  }

  private boolean parseLongArray(LongArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        arrayData.array[arrayData.index] = parseLong(s);
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private long parseLong(String s)
  {
    long value;
    try
    {
      value = Long.parseLong(s);
    }
    catch (NumberFormatException e)
    {
      throw new SimulatorException("Could not parse [%s] as long.", s);
    }
    return value;
  }

  private boolean parseFloatArray(FloatArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        arrayData.array[arrayData.index] = parseFloat(s);
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private float parseFloat(String s)
  {
    float value;
    try
    {
      value = Float.parseFloat(s);
    }
    catch (NumberFormatException e)
    {
      throw new SimulatorException("Could not parse [%s] as float.", s);
    }
    return value;
  }

  private boolean parseDoubleArray(DoubleArrayData arrayData, String string)
  {
    List<String> strings = StringUtil.splitAndTrim(string, ",");
    for (String s : strings)
    {
      if (!s.isEmpty())
      {
        arrayData.array[arrayData.index] = parseDouble(s);
        arrayData.index++;
        if (arrayData.index == arrayData.array.length)
        {
          return true;
        }
      }
    }
    return false;
  }

  private double parseDouble(String s)
  {
    double value;
    try
    {
      value = Double.parseDouble(s);
    }
    catch (NumberFormatException e)
    {
      throw new SimulatorException("Could not parse [%s] as double.", s);
    }
    return value;
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

  private void startMapElementData(MapElementData mapElementData, Map<String, String> attributes)
  {
    mapElementData.load(attributes);
  }

  private void startSetElementData(SetElementData setElementData, Map<String, String> attributes)
  {
    setElementData.load(attributes);
  }

  private void startListElementData(ListElementData listElementData, Map<String, String> attributes)
  {
    listElementData.load(attributes);
  }

  @Override
  public void startElement(String uri, String lName, String qName, Attributes attr)
  {
    if (qName.equals(docName))
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
          else if (o instanceof MapElementData)
          {
            startMapElementData((MapElementData) o, attributeMap);
          }
          else if (o instanceof SetElementData)
          {
            startSetElementData((SetElementData) o, attributeMap);
          }
          else if (o instanceof ListElementData)
          {
            startListElementData((ListElementData) o, attributeMap);
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
    if (characters != null)
    {
      StringBuilder builder = new StringBuilder();
      for (String string : characters)
      {
        builder.append(string);
      }
      characters = null;
      String string = builder.toString();
      builder = null;
      processCharacters(string);
    }

    if (qName.equals(docName))
    {
      dataStack = null;
    }
    else
    {
      if (qName.equals(rootTagName))
      {
        SaveXMLDataField saveDataField = (SaveXMLDataField) dataStack.get(dataStack.size() - 1);
        circuitData = saveDataField.typeInstance;
      }
      else
      {
        if (dataStack.size() - 2 >= 0)
        {
          endElement();
        }
      }
      dataStack.remove(dataStack.size() - 1);
    }
  }

  protected void endElement()
  {
    XMLDataField xmlDataFieldStackMinusOne = dataStack.get(dataStack.size() - 1);
    Map<String, String> attributes = xmlDataFieldStackMinusOne.attributes;
    XMLDataField xmlDataFieldStackMinusTwo = dataStack.get(dataStack.size() - 2);
    if (xmlDataFieldStackMinusTwo instanceof SaveXMLDataField)
    {
      SaveXMLDataField containingClassField = (SaveXMLDataField) xmlDataFieldStackMinusTwo;
      InstanceInspector instanceInspector = new InstanceInspector(containingClassField.typeInstance);

      if (xmlDataFieldStackMinusOne instanceof SaveXMLDataField)
      {
        SaveXMLDataField saveXMLDataField = (SaveXMLDataField) xmlDataFieldStackMinusOne;
        if (containingClassField.typeInstance instanceof ArrayListData)
        {
          ArrayListData listData = (ArrayListData) containingClassField.typeInstance;
          listData.set(saveXMLDataField.typeInstance.getObject());
        }
        else if (containingClassField.typeInstance instanceof LinkedHashMapData)
        {
          LinkedHashMapData mapData = (LinkedHashMapData) containingClassField.typeInstance;
          mapData.set(saveXMLDataField.typeInstance.getObject());
        }
        else if (containingClassField.typeInstance instanceof HashMapData)
        {
          HashMapData mapData = (HashMapData) containingClassField.typeInstance;
          mapData.set(saveXMLDataField.typeInstance.getObject());
        }
        else if (containingClassField.typeInstance instanceof LinkedHashSetData)
        {
          LinkedHashSetData setData = (LinkedHashSetData) containingClassField.typeInstance;
          setData.set(saveXMLDataField.typeInstance.getObject());
        }
        else if (containingClassField.typeInstance instanceof HashSetData)
        {
          HashSetData setData = (HashSetData) containingClassField.typeInstance;
          setData.set(saveXMLDataField.typeInstance.getObject());
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
        else if (containingClassField.typeInstance instanceof IntData)
        {
          IntData data = (IntData) containingClassField.typeInstance;
          instanceInspector.setFieldValue(saveXMLDataField.fieldName, data.x);
        }
        else if (containingClassField.typeInstance instanceof LongData)
        {
          LongData data = (LongData) containingClassField.typeInstance;
          instanceInspector.setFieldValue(saveXMLDataField.fieldName, data.x);
        }
        else if (containingClassField.typeInstance instanceof MapElementData)
        {
          MapElementData data = (MapElementData) containingClassField.typeInstance;
          data.set(((SaveXMLDataField) xmlDataFieldStackMinusOne).typeInstance);
        }
        else if (containingClassField.typeInstance instanceof SetElementData)
        {
          SetElementData data = (SetElementData) containingClassField.typeInstance;
          data.set(((SaveXMLDataField) xmlDataFieldStackMinusOne).typeInstance);
        }
        else if (containingClassField.typeInstance instanceof ListElementData)
        {
          ListElementData data = (ListElementData) containingClassField.typeInstance;
          data.set(((SaveXMLDataField) xmlDataFieldStackMinusOne).typeInstance);
        }
        else if (containingClassField.typeInstance instanceof KeyData)
        {
          KeyData data = (KeyData) containingClassField.typeInstance;
          data.object = ((SaveXMLDataField) xmlDataFieldStackMinusOne).typeInstance.getObject();
        }
        else if (containingClassField.typeInstance instanceof ValueData)
        {
          ValueData data = (ValueData) containingClassField.typeInstance;
          data.object = ((SaveXMLDataField) xmlDataFieldStackMinusOne).typeInstance.getObject();
        }
        else
        {
          instanceInspector.setFieldValue(saveXMLDataField.fieldName, saveXMLDataField.typeInstance.getObject());
        }
      }
      else if (xmlDataFieldStackMinusOne instanceof UnknownXMLDataField)
      {
      }
      else if (xmlDataFieldStackMinusOne instanceof ProcessedXMLDataField)
      {
      }
    }
    else
    {
      throw new SimulatorException("Found class [%s] on stack.  Expected only [SaveXMLDataField].", xmlDataFieldStackMinusTwo.getClass().getSimpleName());
    }
  }
}

