package net.logicim.data.common;

import net.logicim.data.SaveData;
import net.logicim.ui.common.Rotation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RotationData
    extends SaveData
{
  public Rotation rotation;

  public RotationData(Rotation rotation)
  {
    this.rotation = rotation;
  }

  @Override
  public void writeData(Document doc, Element parent)
  {
    Node node = writeString(doc, "rotation", this.rotation.toString());
    parent.appendChild(node);
  }

  public Rotation toRotation()
  {
    return rotation;
  }
}

