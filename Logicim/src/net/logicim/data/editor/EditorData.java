package net.logicim.data.editor;

import net.logicim.data.ReflectiveData;
import net.logicim.data.circuit.CircuitData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class EditorData
    extends ReflectiveData
{
  public CircuitData circuit;
  public boolean running;
  public Rotation creationRotation;
  public List<BookmarkData> subcircuitBookmarks;
}

