package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.Trace;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.SemiconductorView;
import net.logicim.ui.common.port.PortView;

import java.util.List;

public abstract class SemiconductorData
    extends ComponentData
{
 protected List<MultiPortData> ports;

  public SemiconductorData()
  {
  }

  public SemiconductorData(Int2D position,
                           Rotation rotation,
                           String name,
                           List<MultiPortData> ports,
                           boolean selected)
  {
    super(name, position, rotation, selected);
    this.ports = ports;
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, SemiconductorView<?> semiconductorView)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      PortView portView = semiconductorView.getPort(i);
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

  public SemiconductorView<?> createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    SemiconductorView<?> semiconductorView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, semiconductorView);

    if (selected)
    {
      circuitEditor.select(semiconductorView);
    }
    return semiconductorView;
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, SemiconductorView<?> semiconductorView)
  {
    circuitEditor.createConnectionViews(semiconductorView);
    loadPorts(circuitEditor, traceLoader, semiconductorView);
  }

  protected abstract SemiconductorView<?> create(CircuitEditor circuitEditor, TraceLoader traceLoader);

  protected abstract void loadPort(CircuitEditor circuitEditor, PortData portData, Port port);
}

