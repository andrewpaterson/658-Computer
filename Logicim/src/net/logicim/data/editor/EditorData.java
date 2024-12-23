package net.logicim.data.editor;

import net.logicim.data.circuit.CircuitData;
import net.logicim.data.common.ReflectiveData;
import net.logicim.ui.common.Rotation;

import java.util.List;

public class EditorData
    extends ReflectiveData
{
  public CircuitData circuit;
  public long defaultRunTimeStep;
  public long runTimeStep;
  public boolean running;
  public Rotation creationRotation;
  public List<BookmarkData> subcircuitBookmarks;
  public List<SubcircuitParameterData> subcircuitParameters;
  public List<DefaultComponentPropertiesData> defaultProperties;
  public boolean drawPointGrid;

  public EditorData()
  {
  }

  public EditorData(CircuitData circuit,
                    long defaultRunTimeStep,
                    long runTimeStep,
                    boolean running,
                    Rotation creationRotation,
                    List<BookmarkData> subcircuitBookmarks,
                    List<SubcircuitParameterData> subcircuitParameters,
                    List<DefaultComponentPropertiesData> defaultProperties,
                    boolean drawPointGrid)
  {
    this.circuit = circuit;
    this.defaultRunTimeStep = defaultRunTimeStep;
    this.running = running;
    this.runTimeStep = runTimeStep;
    this.creationRotation = creationRotation;
    this.subcircuitBookmarks = subcircuitBookmarks;
    this.subcircuitParameters = subcircuitParameters;
    this.defaultProperties = defaultProperties;
    this.drawPointGrid = drawPointGrid;
  }
}

