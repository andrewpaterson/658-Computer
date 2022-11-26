package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitData<ICV extends IntegratedCircuitView<?>>
    extends ReflectiveData
{
  protected Int2D position;
  protected Rotation rotation;

  protected List<IntegratedCircuitEventData<?>> events;
  protected List<PortData> ports;

  public IntegratedCircuitData()
  {
  }

  public IntegratedCircuitData(Int2D position,
                               Rotation rotation,
                               List<IntegratedCircuitEventData<?>> events,
                               List<PortData> ports)
  {
    this.position = position;
    this.rotation = rotation;
    this.events = events;
    this.ports = ports;
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, ICV integratedCircuitView)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      PortView portView = integratedCircuitView.getPort(i);
      PortData portData = ports.get(i);

      TraceNet trace = traceLoader.create(portData.traceId);
      Port port = portView.getPort();
      port.connect(trace);

      Map<Long, PortEvent> portEventMap = new HashMap<>();
      for (PortEventData<?> eventData : portData.events)
      {
        PortEvent portEvent = eventData.create(port, circuitEditor.getTimeline());
        portEventMap.put(eventData.id, portEvent);
      }

      if ((portData.output != null) && (portData.output.id > 0))
      {
        PortOutputEvent outputPortEvent = (PortOutputEvent) portEventMap.get(portData.output.id);
        if (outputPortEvent == null)
        {
          outputPortEvent = portData.output.create(port, circuitEditor.getTimeline());
        }
        port.setOutput(outputPortEvent);
      }
    }
  }

  protected void loadEvents(CircuitEditor circuitEditor, ICV integratedCircuitView)
  {
    for (IntegratedCircuitEventData<?> event : events)
    {
      event.create(integratedCircuitView.getIntegratedCircuit(), circuitEditor.getTimeline());
    }
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, ICV integratedCircuitView)
  {
    circuitEditor.createConnectionViews(integratedCircuitView);
    loadEvents(circuitEditor, integratedCircuitView);
    loadPorts(circuitEditor, traceLoader, integratedCircuitView);
  }

  public ICV createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    ICV integratedCircuitView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, integratedCircuitView);
    return integratedCircuitView;
  }

  public abstract ICV create(CircuitEditor circuitEditor, TraceLoader traceLoader);

}

