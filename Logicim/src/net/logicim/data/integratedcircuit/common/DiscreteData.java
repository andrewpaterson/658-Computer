package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.Trace;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.common.port.PortView;

import java.util.List;

public abstract class DiscreteData
    extends ReflectiveData
{
  protected String name;

  protected Int2D position;
  protected Rotation rotation;

  protected List<MultiPortData> ports;

  protected boolean selected;

  public DiscreteData()
  {
  }

  public DiscreteData(Int2D position,
                      Rotation rotation,
                      String name,
                      List<MultiPortData> ports,
                      boolean selected)
  {
    this.position = new Int2D(position);
    this.rotation = rotation;
    this.name = name;
    this.ports = ports;
    this.selected = selected;
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, DiscreteView<?> discreteView)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      PortView portView = discreteView.getPort(i);
      MultiPortData multiPortData = ports.get(i);

      List<Port> ports = portView.getPorts();
      for (int j = 0; j < ports.size(); j++)
      {
        Port port = ports.get(j);
        PortData portData = multiPortData.ports.get(j);
        Trace trace = traceLoader.create(portData.traceId);
        port.connect(trace);

        loadPort(circuitEditor, portData, port);
      }
    }
  }

  public DiscreteView<?> createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    DiscreteView<?> discreteView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, discreteView);

    if (selected)
    {
      circuitEditor.select(discreteView);
    }
    return discreteView;
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, DiscreteView<?> discreteView)
  {
    circuitEditor.createConnectionViews(discreteView);
    loadPorts(circuitEditor, traceLoader, discreteView);
  }

  protected abstract DiscreteView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader);

  protected abstract void loadPort(CircuitEditor circuitEditor, PortData portData, Port port);
}

