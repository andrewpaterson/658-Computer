package net.logicim.data;

import net.logicim.common.SimulatorException;
import net.logicim.common.reflect.InstanceInspector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.List;

public abstract class SaveData
{
  protected String getXMLTag()
  {
    String simpleName = getClass().getSimpleName();
    if (simpleName.endsWith("Data"))
    {
      simpleName = simpleName.substring(0, simpleName.length() - 4);
    }
    return simpleName;
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

  public void writeXML(Document doc, Node parent)
  {
    Element element = doc.createElement(getXMLTag());
    parent.appendChild(element);

    writeData(doc, element);
  }

  public void writeXML(Document doc, String elementName, Node parent)
  {
    Element element = doc.createElement(elementName);
    element.setAttribute("Type", getXMLTag());
    parent.appendChild(element);

    writeData(doc, element);
  }

  public void writeList(Document doc, Element parent, String name, List<?> list)
  {
    int size = list.size();

    Element listContainer = doc.createElement(name);
    listContainer.setAttribute("size", Integer.toString(size));
    parent.appendChild(listContainer);

    for (int i = 0; i < size; i++)
    {
      SaveData saveData = (SaveData) list.get(i);
      Element element = doc.createElement("element");
      element.setAttribute("Type", saveData.getXMLTag());
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
      else if (fieldValue instanceof SaveData)
      {
        SaveData saveData = (SaveData) fieldValue;
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
      else
      {
        throw new SimulatorException("Unknown field class [" + fieldValue.getClass().getSimpleName() + "] writing field [" + getClass().getSimpleName() + "." + fieldName + "].");
      }
    }
  }
}

