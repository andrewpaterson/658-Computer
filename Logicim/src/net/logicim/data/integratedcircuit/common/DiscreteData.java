package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

import java.util.List;

public abstract class DiscreteData
    extends ReflectiveData
{
  protected Int2D position;
  protected Rotation rotation;
  protected String name;
  protected List<PortData> ports;

  public DiscreteData()
  {
  }

  public DiscreteData(Int2D position, Rotation rotation, String name, List<PortData> ports)
  {
    this.position = position;
    this.rotation = rotation;
    this.name = name;
    this.ports = ports;
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, DiscreteView discreteView)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      PortView portView = discreteView.getPort(i);
      PortData portData = ports.get(i);

      TraceNet trace = traceLoader.create(portData.traceId);
      Port port = portView.getPort();
      port.connect(trace);

      loadPort(circuitEditor, portData, port);
    }
  }

  public DiscreteView createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    DiscreteView integratedCircuitView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, integratedCircuitView);
    return integratedCircuitView;
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, DiscreteView discreteView)
  {
    circuitEditor.createConnectionViews(discreteView);
    loadPorts(circuitEditor, traceLoader, discreteView);
  }

  protected abstract DiscreteView create(CircuitEditor circuitEditor, TraceLoader traceLoader);

  protected abstract void loadPort(CircuitEditor circuitEditor, PortData portData, Port port);
}

