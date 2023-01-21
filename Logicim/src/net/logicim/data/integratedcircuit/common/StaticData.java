package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;

public abstract class StaticData
    extends ReflectiveData
{
  protected String name;
  protected Int2D position;
  protected Rotation rotation;
  protected boolean selected;

  public StaticData()
  {
  }

  public StaticData(String name, Int2D position, Rotation rotation, boolean selected)
  {
    this.name = name;
    this.position = position;
    this.rotation = rotation;
    this.selected = selected;
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, StaticView<?> componentView)
  {
    circuitEditor.createConnectionViewsFromComponentPorts(componentView);
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

