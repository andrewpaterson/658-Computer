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
      Object fieldValue = instanceInspector.getFieldValue(fieldName);
      if (fieldValue == null)
      {
      }
      else if (fieldValue instanceof ReflectiveData)
      {
        ReflectiveData saveData = (ReflectiveData) fieldValue;
        saveData.writeXML(doc, fieldName, parent);
      }
      else if (fieldValue instanceof String)
      {
        Node node = writeString(doc, fieldName, (String) fieldValue);
        parent.appendChild(node);
      }
      else if (fieldValue instanceof Long)
      {
        Node node = writeLong(doc, fieldName, (Long) fieldValue);
        parent.appendChild(node);
      }
      else if (fieldValue instanceof Integer)
      {
        Node node = writeInt(doc, fieldName, (Integer) fieldValue);
        parent.appendChild(node);
      }
      else if (fieldValue instanceof Float)
      {
        Node node = writeFloat(doc, fieldName, (Float) fieldValue);
        parent.appendChild(node);
      }
      else if (fieldValue instanceof Boolean)
      {
        Node node = writeBoolean(doc, fieldName, (Boolean) fieldValue);
        parent.appendChild(node);
      }
      else if (fieldValue instanceof List)
      {
        writeList(doc, parent, fieldName, (List<?>) fieldValue);
      }
      else if (fieldValue instanceof Int2D)
      {
        writeInt2D(doc, parent, fieldName, (Int2D) fieldValue);
      }
      else if (fieldValue instanceof Rotation)
      {
        writeRotation(doc, parent, fieldName, (Rotation) fieldValue);
      }
      else
      {
        throw new SimulatorException("Unknown field class [" + fieldValue.getClass().getSimpleName() + "] writing field [" + getClass().getSimpleName() + "." + fieldName + "].");
      }
    }
  }
}

