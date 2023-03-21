package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.port.common.LogicPortData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.state.State;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitData<ICV extends IntegratedCircuitView<?, ?>, STATE extends State>
    extends ComponentData<ICV>
{
  protected Family family;

  protected List<IntegratedCircuitEventData<?>> events;

  protected STATE state;

  public IntegratedCircuitData()
  {
  }

  public IntegratedCircuitData(Int2D position,
                               Rotation rotation,
                               String name,
                               Family family,
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
  protected void loadPort(Simulation simulation, PortData portData, Port port)
  {
    if (port.isLogicPort())
    {
      LogicPortData logicPortData = (LogicPortData) portData;
      LogicPort logicPort = (LogicPort) port;
      Map<Long, PortEvent> portEventMap = new HashMap<>();
      for (PortEventData<?> eventData : logicPortData.events)
      {
        PortEvent portEvent = eventData.create(logicPort, simulation.getTimeline());
        portEventMap.put(eventData.id, portEvent);
      }

      if ((logicPortData.output != null) && (logicPortData.output.id > 0))
      {
        PortOutputEvent outputPortEvent = (PortOutputEvent) portEventMap.get(logicPortData.output.id);
        if (outputPortEvent == null)
        {
          outputPortEvent = logicPortData.output.create(logicPort, simulation.getTimeline());
        }
        logicPort.setOutput(outputPortEvent);
      }
    }
  }

  protected void loadEvents(Simulation simulation, ICV integratedCircuitView)
  {
    for (IntegratedCircuitEventData<?> event : events)
    {
      event.create(integratedCircuitView.getIntegratedCircuit(), simulation.getTimeline());
    }
  }

  @Override
  protected void connectAndLoad(SubcircuitView subcircuitView, Simulation simulation, TraceLoader traceLoader, ICV integratedCircuitView)
  {
    integratedCircuitView.createConnections(subcircuitView);
    integratedCircuitView.enable(simulation);

    loadState(integratedCircuitView);
    loadEvents(simulation, integratedCircuitView);
    loadPorts(traceLoader, integratedCircuitView, simulation);
  }

  private void loadState(ICV integratedCircuitView)
  {
    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    integratedCircuit.setState(state);
  }

  public abstract ICV create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader);
}

