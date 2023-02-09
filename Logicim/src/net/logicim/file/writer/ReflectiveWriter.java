package net.logicim.file.writer;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.ReflectiveData;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.List;

public abstract class ReflectiveWriter
{
  public static final int ARRAY_ROW_LENGTH = 3;

  public static final String LOGICIM_TAG_NAME = "Logicim";
  public static final String INDEX = "index";
  public static final String CIRCUIT_DATA_TAG_NAME = "circuitData";
  public static final String TYPE = "type";

  public static String getXMLTag(Object o)
  {
    Class<?> aClass = o.getClass();
    return getXMLTag(aClass);
  }

  public static String getXMLTag(Class<?> aClass)
  {
    String simpleName = aClass.getSimpleName();
    if (simpleName.endsWith("Data"))
    {
      simpleName = simpleName.substring(0, simpleName.length() - 4);
    }
    return simpleName;
  }

  public static Node writeString(Document doc, String elementName, String value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(value));
    return node;
  }

  public static Node writeLong(Document doc, String elementName, long value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Long.toString(value)));
    return node;
  }

  public static Node writeInt(Document doc, String elementName, int value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Integer.toString(value)));
    return node;
  }

  public static Node writeFloat(Document doc, String elementName, float value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Float.toString(value)));
    return node;
  }

  public static Node writeDouble(Document doc, String elementName, double value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Double.toString(value)));
    return node;
  }

  public static Node writeBoolean(Document doc, String elementName, boolean value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Boolean.toString(value)));
    return node;
  }

  public static void writeInt2D(Document doc, Element parent, String elementName, Int2D p)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Int2D.class.getSimpleName());
    node.setAttribute("x", Integer.toString(p.x));
    node.setAttribute("y", Integer.toString(p.y));
    parent.appendChild(node);
  }

  public static void writeFloat2D(Document doc, Element parent, String elementName, Float2D p)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Float2D.class.getSimpleName());
    node.setAttribute("x", Float.toString(p.x));
    node.setAttribute("y", Float.toString(p.y));
    parent.appendChild(node);
  }

  public static void writeIntArray(Document doc, Element parent, String elementName, int[] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, int[].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    StringBuilder builder = new StringBuilder();
    if (fieldValue.length > 0)
    {
      builder.append("\n");
      for (int i = 0; i < fieldValue.length; i++)
      {
        if ((i % ARRAY_ROW_LENGTH == 0) && (i != 0))
        {
          builder.append("\n");
        }
        builder.append(fieldValue[i]);
        builder.append(",");
      }
      int length = builder.length();
      builder.delete(length - 1, length);
    }
    arrayContainer.appendChild(doc.createTextNode(builder.toString()));
  }

  public static void writeInt2DArray(Document doc, Element parent, String elementName, int[][] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, int[][].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    for (int i = 0; i < fieldValue.length; i++)
    {
      writeIntArray(doc, arrayContainer, "array" + i, fieldValue[i]);
    }
  }

  public static void writeLongArray(Document doc, Element parent, String elementName, long[] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, long[].class.getSimpleName());
    arrayContainer.setAttribute("length", Long.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    StringBuilder builder = new StringBuilder();
    if (fieldValue.length > 0)
    {
      builder.append("\n");
      for (int i = 0; i < fieldValue.length; i++)
      {
        if ((i % ARRAY_ROW_LENGTH == 0) && (i != 0))
        {
          builder.append("\n");
        }
        builder.append(fieldValue[i]);
        builder.append(",");
      }
      int length = builder.length();
      builder.delete(length - 1, length);
    }
    arrayContainer.appendChild(doc.createTextNode(builder.toString()));
  }

  public static void writeLong2DArray(Document doc, Element parent, String elementName, long[][] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, long[][].class.getSimpleName());
    arrayContainer.setAttribute("length", Long.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    for (int i = 0; i < fieldValue.length; i++)
    {
      writeLongArray(doc, arrayContainer, "array" + i, fieldValue[i]);
    }
  }

  public static void writeFloatArray(Document doc, Element parent, String elementName, float[] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, float[].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    StringBuilder builder = new StringBuilder();
    if (fieldValue.length > 0)
    {
      builder.append("\n");
      for (int i = 0; i < fieldValue.length; i++)
      {
        if ((i % ARRAY_ROW_LENGTH == 0) && (i != 0))
        {
          builder.append("\n");
        }
        builder.append(fieldValue[i]);
        builder.append(",");
      }
      int length = builder.length();
      builder.delete(length - 1, length);
    }
    arrayContainer.appendChild(doc.createTextNode(builder.toString()));
  }

  public static void writeFloat2DArray(Document doc, Element parent, String elementName, float[][] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, float[][].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    for (int i = 0; i < fieldValue.length; i++)
    {
      writeFloatArray(doc, arrayContainer, "array" + i, fieldValue[i]);
    }
  }

  public static void writeDoubleArray(Document doc, Element parent, String elementName, double[] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, double[].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    StringBuilder builder = new StringBuilder();
    if (fieldValue.length > 0)
    {
      builder.append("\n");
      for (int i = 0; i < fieldValue.length; i++)
      {
        if ((i % ARRAY_ROW_LENGTH == 0) && (i != 0))
        {
          builder.append("\n");
        }
        builder.append(fieldValue[i]);
        builder.append(",");
      }
      int length = builder.length();
      builder.delete(length - 1, length);
    }
    arrayContainer.appendChild(doc.createTextNode(builder.toString()));
  }

  public static void writeEnum(Document doc, Element parent, String elementName, Class<?> fieldClass, Enum<?> fieldValue)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Enum.class.getSimpleName());
    node.setAttribute("class", fieldClass.getSimpleName());
    node.setAttribute("enum", StringUtil.toEnumString(fieldValue));
    parent.appendChild(node);
  }

  public static void writeDouble2DArray(Document doc, Element parent, String elementName, double[][] fieldValue)
  {
    Element arrayContainer = doc.createElement(elementName);
    arrayContainer.setAttribute(TYPE, double[][].class.getSimpleName());
    arrayContainer.setAttribute("length", Integer.toString(fieldValue.length));
    parent.appendChild(arrayContainer);

    for (int i = 0; i < fieldValue.length; i++)
    {
      writeDoubleArray(doc, arrayContainer, "array" + i, fieldValue[i]);
    }
  }

  public static void writeRotation(Document doc, Element parent, String elementName, Rotation r)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Rotation.class.getSimpleName());
    node.setAttribute("rotation", r.toString());
    parent.appendChild(node);
  }

  public static void writeXML(Object o, Document doc, Node parent)
  {
    Element element = doc.createElement(getXMLTag(o));
    parent.appendChild(element);

    writeData(o, doc, element);
  }

  public static void writeXML(Object o, Document doc, String elementName, Node parent)
  {
    Element element = doc.createElement(elementName);
    element.setAttribute(TYPE, getXMLTag(o));
    parent.appendChild(element);

    writeData(o, doc, element);
  }

  public static void writeList(Document doc, Element parent, String name, List<?> list)
  {
    int size = list.size();

    Element listContainer = doc.createElement(name);
    listContainer.setAttribute(TYPE, list.getClass().getSimpleName());
    listContainer.setAttribute("size", Integer.toString(size));
    parent.appendChild(listContainer);

    for (int i = 0; i < size; i++)
    {
      ReflectiveData saveData = (ReflectiveData) list.get(i);
      Element element = doc.createElement("element");
      element.setAttribute(TYPE, getXMLTag(saveData));
      element.setAttribute("index", Integer.toString(i));
      listContainer.appendChild(element);
      writeData(saveData, doc, element);
    }
  }

  public static void writeData(Object o, Document doc, Element parent)
  {
    writeReflectiveData(o, doc, parent);
  }

  public static void writeReflectiveData(Object o, Document doc, Element parent)
  {
    InstanceInspector instanceInspector = new InstanceInspector(o);
    Collection<String> fieldNames = instanceInspector.getFieldNames();
    for (String fieldName : fieldNames)
    {
      Class<?> fieldClass = instanceInspector.getFieldClass(fieldName);
      Object fieldValue = instanceInspector.getFieldValue(fieldName);
      if (ReflectiveData.class.isAssignableFrom(fieldClass))
      {
        writeReflectiveData(doc, parent, fieldName, (ReflectiveData) fieldValue);
      }
      else if (State.class.isAssignableFrom(fieldClass))
      {
        writeStateData(doc, parent, fieldName, (State) fieldValue);
      }
      else if (String.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeString(doc, fieldName, (String) fieldValue));
      }
      else if (Long.class.isAssignableFrom(fieldClass) || long.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeLong(doc, fieldName, (Long) fieldValue));
      }
      else if (Integer.class.isAssignableFrom(fieldClass) || int.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeInt(doc, fieldName, (Integer) fieldValue));
      }
      else if (Float.class.isAssignableFrom(fieldClass) || float.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeFloat(doc, fieldName, (Float) fieldValue));
      }
      else if (Double.class.isAssignableFrom(fieldClass) || double.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeDouble(doc, fieldName, (Double) fieldValue));
      }
      else if (Boolean.class.isAssignableFrom(fieldClass) || boolean.class.isAssignableFrom(fieldClass))
      {
        parent.appendChild(writeBoolean(doc, fieldName, (Boolean) fieldValue));
      }
      else if (List.class.isAssignableFrom(fieldClass))
      {
        writeList(doc, parent, fieldName, (List<?>) fieldValue);
      }
      else if (Int2D.class.isAssignableFrom(fieldClass))
      {
        writeInt2D(doc, parent, fieldName, (Int2D) fieldValue);
      }
      else if (Float2D.class.isAssignableFrom(fieldClass))
      {
        writeFloat2D(doc, parent, fieldName, (Float2D) fieldValue);
      }
      else if (Rotation.class.isAssignableFrom(fieldClass))
      {
        writeRotation(doc, parent, fieldName, (Rotation) fieldValue);
      }
      else if (int[][].class.isAssignableFrom(fieldClass))
      {
        writeInt2DArray(doc, parent, fieldName, (int[][]) fieldValue);
      }
      else if (long[][].class.isAssignableFrom(fieldClass))
      {
        writeLong2DArray(doc, parent, fieldName, (long[][]) fieldValue);
      }
      else if (float[][].class.isAssignableFrom(fieldClass))
      {
        writeFloat2DArray(doc, parent, fieldName, (float[][]) fieldValue);
      }
      else if (double[][].class.isAssignableFrom(fieldClass))
      {
        writeDouble2DArray(doc, parent, fieldName, (double[][]) fieldValue);
      }
      else if (int[].class.isAssignableFrom(fieldClass))
      {
        writeIntArray(doc, parent, fieldName, (int[]) fieldValue);
      }
      else if (long[].class.isAssignableFrom(fieldClass))
      {
        writeLongArray(doc, parent, fieldName, (long[]) fieldValue);
      }
      else if (float[].class.isAssignableFrom(fieldClass))
      {
        writeFloatArray(doc, parent, fieldName, (float[]) fieldValue);
      }
      else if (double[].class.isAssignableFrom(fieldClass))
      {
        writeDoubleArray(doc, parent, fieldName, (double[]) fieldValue);
      }
      else if (Enum.class.isAssignableFrom(fieldClass))
      {
        writeEnum(doc, parent, fieldName, fieldClass, (Enum<?>) fieldValue);
      }
      else
      {
        throw new SimulatorException("Unknown field class [" + fieldClass.getSimpleName() + "] writing field [" + o.getClass().getSimpleName() + "." + fieldName + "] with value [" + fieldValue + "].");
      }
    }
  }

  protected static void writeReflectiveData(Document doc, Element parent, String fieldName, ReflectiveData reflectiveData)
  {
    if (reflectiveData != null)
    {
      writeXML(reflectiveData, doc, fieldName, parent);
    }
  }

  protected static void writeStateData(Document doc, Element parent, String fieldName, State state)
  {
    if (state != null)
    {
      writeXML(state, doc, fieldName, parent);
    }
  }
}

