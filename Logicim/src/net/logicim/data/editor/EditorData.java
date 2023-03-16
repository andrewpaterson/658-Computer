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
  public long runTimeStep;
  public Rotation creationRotation;
  public List<BookmarkData> subcircuitBookmarks;
  public List<SubcircuitParameterData> subcircuitParameters;

  public EditorData(CircuitData circuit,
                    boolean running,
                    long runTimeStep,
                    Rotation creationRotation,
                    List<BookmarkData> subcircuitBookmarks,
                    List<SubcircuitParameterData> subcircuitParameters)
  {
    this.circuit = circuit;
    this.running = running;
    this.runTimeStep = runTimeStep;
    this.creationRotation = creationRotation;
    this.subcircuitBookmarks = subcircuitBookmarks;
    this.subcircuitParameters = subcircuitParameters;
  }
}

