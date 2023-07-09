package net.logicim.data.integratedcircuit.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.event.IntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.MultiIntegratedCircuitEventData;
import net.logicim.data.integratedcircuit.event.SimulationIntegratedCircuitEventData;
import net.logicim.data.port.common.LogicPortData;
import net.logicim.data.port.common.PortData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.simulation.SimulationStateData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.state.State;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.simulation.CircuitLoaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IntegratedCircuitData<ICV extends IntegratedCircuitView<?, ?>, STATE extends State>
    extends ComponentData<ICV>
{
  public Family family;

  public SimulationIntegratedCircuitEventData events;
  public SimulationStateData<STATE> state;

  public IntegratedCircuitData()
  {
  }

  public IntegratedCircuitData(Int2D position,
                               Rotation rotation,
                               String name,
                               Family family,
                               SimulationIntegratedCircuitEventData events,
                               List<SimulationMultiPortData> ports,
                               long id,
                               boolean enabled,
                               boolean selected,
                               SimulationStateData<STATE> state)
  {
    super(position,
          rotation,
          name,
          ports,
          id,
          enabled,
          selected);
    this.family = family;
    this.events = events;
    this.state = state;
  }

  @Override
  protected void loadPort(SubcircuitSimulation subcircuitSimulation, PortData portData, Port port)
  {
    if (port.isLogicPort())
    {
      LogicPortData logicPortData = (LogicPortData) portData;
      LogicPort logicPort = (LogicPort) port;
      Map<Long, PortEvent> portEventMap = new HashMap<>();
      for (PortEventData<?> eventData : logicPortData.events)
      {
        PortEvent portEvent = eventData.create(logicPort, subcircuitSimulation.getTimeline());
        portEventMap.put(eventData.id, portEvent);
      }

      if ((logicPortData.output != null) && (logicPortData.output.id > 0))
      {
        PortOutputEvent outputPortEvent = (PortOutputEvent) portEventMap.get(logicPortData.output.id);
        if (outputPortEvent == null)
        {
          outputPortEvent = logicPortData.output.create(logicPort, subcircuitSimulation.getTimeline());
        }
        logicPort.setOutput(outputPortEvent);
      }
    }
  }

  protected void loadEvents(SubcircuitSimulation subcircuitSimulation, ICV integratedCircuitView)
  {
    List<? extends IntegratedCircuitEventData<?>> integratedCircuitEventData = getIntegratedCircuitEventDataList(subcircuitSimulation.getId());
    if (integratedCircuitEventData == null)
    {
      throw new SimulatorException("Cannot find IntegratedCircuitEventData for simulation ID [%s].", subcircuitSimulation.getId());
    }
    for (IntegratedCircuitEventData<?> eventData : integratedCircuitEventData)
    {
      IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getComponent(subcircuitSimulation);
      eventData.create(integratedCircuit, subcircuitSimulation.getTimeline());
    }
  }

  protected List<? extends IntegratedCircuitEventData<?>> getIntegratedCircuitEventDataList(long simulationId)
  {
    for (Map.Entry<Long, MultiIntegratedCircuitEventData> entry : events.simulationIntegratedCircuitEventData.entrySet())
    {
      if (simulationId == entry.getKey())
      {
        return entry.getValue().integratedCircuitEventData;
      }
    }
    return null;
  }

  protected STATE getState(long simulationId)
  {
    return state.simulationStateData.get(simulationId);
  }

  @Override
  public void createAndConnectComponent(SubcircuitSimulation containingSubcircuitSimulation,
                                        CircuitLoaders circuitLoaders,
                                        ICV integratedCircuitView)
  {
    integratedCircuitView.createComponent(containingSubcircuitSimulation);

    loadState(containingSubcircuitSimulation, integratedCircuitView);
    loadEvents(containingSubcircuitSimulation, integratedCircuitView);
    loadPorts(containingSubcircuitSimulation, circuitLoaders, integratedCircuitView);
  }

  private void loadState(SubcircuitSimulation subcircuitSimulation, ICV integratedCircuitView)
  {
    STATE state = getState(subcircuitSimulation.getId());
    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getComponent(subcircuitSimulation);
    integratedCircuit.setState(state);
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return state.simulationStateData.containsKey(id);
  }
}

