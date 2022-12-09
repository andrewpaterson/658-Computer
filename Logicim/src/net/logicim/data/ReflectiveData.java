package net.logicim.data;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.List;

public abstract class ReflectiveData
    extends SaveData
{
  public static final String LOGICIM_TAG_NAME = "Logicim";
  public static final String TYPE = "Type";
  public static final String INDEX = "index";
  public static final String CIRCUIT_DATA_TAG_NAME = "circuitData";

  public ReflectiveData()
  {
  }

  @Override
  public Object getObject()
  {
    return this;
  }

  protected String getXMLTag()
  {
    Class<?> aClass = getClass();
    return getXMLTag(aClass);
  }

  public Node writeString(Document doc, String elementName, String value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(value));
    return node;
  }

  public Node writeLong(Document doc, String elementName, long value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Long.toString(value)));
    return node;
  }

  public Node writeInt(Document doc, String elementName, int value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Integer.toString(value)));
    return node;
  }

  public Node writeFloat(Document doc, String elementName, float value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Float.toString(value)));
    return node;
  }

  public Node writeDouble(Document doc, String elementName, double value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Double.toString(value)));
    return node;
  }

  public Node writeBoolean(Document doc, String elementName, boolean value)
  {
    Element node = doc.createElement(elementName);
    node.appendChild(doc.createTextNode(Boolean.toString(value)));
    return node;
  }

  public void writeInt2D(Document doc, Element parent, String elementName, Int2D p)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Int2D.class.getSimpleName());
    node.setAttribute("x", Integer.toString(p.x));
    node.setAttribute("y", Integer.toString(p.y));
    parent.appendChild(node);
  }

  private void writeIntArray(Document doc, Element parent, String elementName, int[] fieldValue)
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
        if ((i % 20 == 0) && (i != 0))
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
  private void writeInt2DArray(Document doc, Element parent, String elementName, int[][] fieldValue)
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

  private void writeFloatArray(Document doc, Element parent, String elementName, float[] fieldValue)
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
        if ((i % 20 == 0) && (i != 0))
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

  private void writeFloat2DArray(Document doc, Element parent, String elementName, float[][] fieldValue)
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

  public void writeRotation(Document doc, Element parent, String elementName, Rotation r)
  {
    Element node = doc.createElement(elementName);
    node.setAttribute(TYPE, Rotation.class.getSimpleName());
    node.setAttribute("rotation", r.toString());
    parent.appendChild(node);
  }

  public void writeXML(Document doc, Node parent)
  {
    Element element = doc.createElement(getXMLTag());
    parent.appendChild(element);

    writeData(doc, element);
  }

  public void writeXML(Document doc, String elementName, Node parent)
  {
    Element element = doc.createElement(elementName);
    element.setAttribute(TYPE, getXMLTag());
    parent.appendChild(element);

    writeData(doc, element);
  }

  public void writeList(Document doc, Element parent, String name, List<?> list)
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
      element.setAttribute(TYPE, saveData.getXMLTag());
      element.setAttribute("index", Integer.toString(i));
      listContainer.appendChild(element);
      saveData.writeData(doc, element);
    }
  }

  public void writeData(Document doc, Element parent)
  {
    writeReflectiveData(doc, parent);
  }

  public void writeReflectiveData(Document doc, Element parent)
  {
    InstanceInspector instanceInspector = new InstanceInspector(this);
    Collection<String> fieldNames = instanceInspector.getFieldNames();
    for (String fieldName : fieldNames)
    {
      Class<?> fieldClass = instanceInspector.getFieldClass(fieldName);
      Object fieldValue = instanceInspector.getFieldValue(fieldName);
      if (ReflectiveData.class.isAssignableFrom(fieldClass))
      {
        writeReflectiveData(doc, parent, fieldName, (ReflectiveData) fieldValue);
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
      else if (Rotation.class.isAssignableFrom(fieldClass))
      {
        writeRotation(doc, parent, fieldName, (Rotation) fieldValue);
      }
      else if (float[][].class.isAssignableFrom(fieldClass))
      {
        writeFloat2DArray(doc, parent, fieldName, (float[][]) fieldValue);
      }
      else if (int[][].class.isAssignableFrom(fieldClass))
      {
        writeInt2DArray(doc, parent, fieldName, (int[][]) fieldValue);
      }
      else
      {
        throw new SimulatorException("Unknown field class [" + fieldClass.getSimpleName() + "] writing field [" + getClass().getSimpleName() + "." + fieldName + "] with value [" + fieldValue + "].");
      }
    }
  }

  protected void writeReflectiveData(Document doc, Element parent, String fieldName, ReflectiveData reflectiveData)
  {
    if (reflectiveData != null)
    {
      reflectiveData.writeXML(doc, fieldName, parent);
    }
  }
}

