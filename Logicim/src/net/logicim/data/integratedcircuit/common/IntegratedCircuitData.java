package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.LogicPortData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitEditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitData<ICV extends IntegratedCircuitView<?, ?>, STATE extends State>
    extends ComponentData<ICV>
{
  protected String family;

  protected List<IntegratedCircuitEventData<?>> events;

  protected STATE state;

  public IntegratedCircuitData()
  {
  }

  public IntegratedCircuitData(Int2D position,
                               Rotation rotation,
                               String name,
                               String family,
                               List<IntegratedCircuitEventData<?>> events,
                               List<MultiPortData> ports,
                               boolean selected,
                               STATE state)
  {
    super(position, rotation, name, ports, selected);
    this.family = family;
    this.events = events;
    this.state = state;
  }

  @Override
  protected void loadPort(CircuitEditor circuitEditor, PortData portData, Port port)
  {
    if (port.isLogicPort())
    {
      LogicPortData logicPortData = (LogicPortData) portData;
      LogicPort logicPort = (LogicPort) port;
      Map<Long, PortEvent> portEventMap = new HashMap<>();
      for (PortEventData<?> eventData : logicPortData.events)
      {
        PortEvent portEvent = eventData.create(logicPort, circuitEditor.getTimeline());
        portEventMap.put(eventData.id, portEvent);
      }

      if ((logicPortData.output != null) && (logicPortData.output.id > 0))
      {
        PortOutputEvent outputPortEvent = (PortOutputEvent) portEventMap.get(logicPortData.output.id);
        if (outputPortEvent == null)
        {
          outputPortEvent = logicPortData.output.create(logicPort, circuitEditor.getTimeline());
        }
        logicPort.setOutput(outputPortEvent);
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

  @Override
  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, ICV integratedCircuitView)
  {
    integratedCircuitView.createConnections(circuitEditor);
    integratedCircuitView.enable(circuitEditor.getSimulation());

    loadState(integratedCircuitView);
    loadEvents(circuitEditor, integratedCircuitView);
    loadPorts(circuitEditor, traceLoader, integratedCircuitView);
  }

  private void loadState(ICV integratedCircuitView)
  {
    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    integratedCircuit.setState(state);
  }

  public abstract ICV create(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

