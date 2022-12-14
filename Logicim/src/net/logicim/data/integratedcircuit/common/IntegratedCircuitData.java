package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.BasePort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitData<ICV extends IntegratedCircuitView<?>, STATE extends State>
    extends ReflectiveData
{
  protected Int2D position;
  protected Rotation rotation;
  protected String name;
  protected String family;

  protected List<IntegratedCircuitEventData<?>> events;
  protected List<PortData> ports;

  protected STATE state;

  public IntegratedCircuitData()
  {
  }

  public IntegratedCircuitData(Int2D position,
                               Rotation rotation,
                               String name,
                               String family,
                               List<IntegratedCircuitEventData<?>> events,
                               List<PortData> ports,
                               STATE state)
  {
    this.position = position;
    this.rotation = rotation;
    this.name = name;
    this.family = family;
    this.events = events;
    this.ports = ports;
    this.state = state;
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, ICV integratedCircuitView)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      PortView portView = integratedCircuitView.getPort(i);
      PortData portData = ports.get(i);

      TraceNet trace = traceLoader.create(portData.traceId);
      BasePort port = portView.getPort();
      port.connect(trace);

      if (port.isLogicPort())
      {
        Port logicPort = (Port) port;
        Map<Long, PortEvent> portEventMap = new HashMap<>();
        for (PortEventData<?> eventData : portData.events)
        {
          PortEvent portEvent = eventData.create(logicPort, circuitEditor.getTimeline());
          portEventMap.put(eventData.id, portEvent);
        }

        if ((portData.output != null) && (portData.output.id > 0))
        {
          PortOutputEvent outputPortEvent = (PortOutputEvent) portEventMap.get(portData.output.id);
          if (outputPortEvent == null)
          {
            outputPortEvent = portData.output.create(logicPort, circuitEditor.getTimeline());
          }
          logicPort.setOutput(outputPortEvent);
        }
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
    loadState(integratedCircuitView);
    loadEvents(circuitEditor, integratedCircuitView);
    loadPorts(circuitEditor, traceLoader, integratedCircuitView);
  }

  private void loadState(ICV integratedCircuitView)
  {
    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    integratedCircuit.setState(state);
  }

  public ICV createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    ICV integratedCircuitView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, integratedCircuitView);
    return integratedCircuitView;
  }

  public abstract ICV create(CircuitEditor circuitEditor, TraceLoader traceLoader);

}

