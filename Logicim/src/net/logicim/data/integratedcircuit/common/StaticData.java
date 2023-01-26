package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitEditor;

public abstract class StaticData
    extends ReflectiveData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBoxData boundingBox;
  protected BoundingBoxData selectionBox;
  protected boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name,
                    Int2D position,
                    Rotation rotation,
                    BoundingBoxData boundingBox,
                    BoundingBoxData selectionBox,
                    boolean selected)
  {
    this.name = name;
    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = boundingBox;
    this.selectionBox = selectionBox;
    this.selected = selected;
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, StaticView<?> componentView)
  {
    componentView.createConnections(circuitEditor);
    componentView.enable(circuitEditor.getSimulation());

    loadPorts(circuitEditor, traceLoader, componentView);
  }

  public void createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    StaticView<?> componentView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, componentView);

    if (selected)
    {
      circuitEditor.select(componentView);
    }
  }

  protected abstract void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, StaticView<?> componentView);

  protected abstract StaticView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

