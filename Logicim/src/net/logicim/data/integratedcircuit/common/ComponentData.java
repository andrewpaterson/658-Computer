package net.logicim.data.integratedcircuit.common;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.data.port.common.LogicPortData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.PortData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.CircuitLoaders;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentData<T extends ComponentView<?>>
    extends StaticData<T>
{
  protected List<SimulationMultiPortData> ports;

  public ComponentData()
  {
  }

  public ComponentData(Int2D position,
                       Rotation rotation,
                       String name,
                       List<SimulationMultiPortData> ports,
                       long id,
                       boolean enabled,
                       boolean selected)
  {
    super(name,
          position,
          rotation,
          id,
          enabled,
          selected);
    this.ports = ports;
  }

  @Override
  public T createStaticView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    T componentView = createComponentView(subcircuitEditor);
    componentView.getOrCreateConnectionViews(subcircuitEditor.getInstanceSubcircuitView());
    return componentView;
  }

  protected void loadPorts(SubcircuitSimulation subcircuitSimulation, CircuitLoaders circuitLoaders, T componentView)
  {
    List<PortView> portViews = componentView.getPortViews();
    for (int i = 0; i < ports.size(); i++)
    {
      SimulationMultiPortData simulationMultiPortData = ports.get(i);
      PortView portView = portViews.get(i);

      MultiPortData multiPortData = getMultiPortDataForSimulationId(simulationMultiPortData, subcircuitSimulation.getId());
      if (multiPortData == null)
      {
        throw new SimulatorException("Could not find MultiPortData for simulation ID [%s].", subcircuitSimulation.getId());
      }

      int multiPortSize = multiPortData.ports.size();
      List<? extends Port> ports = portView.getPorts(subcircuitSimulation);
      for (int j = 0; j < ports.size(); j++)
      {
        Port port = ports.get(j);
        if (j < multiPortSize)
        {
          PortData portData = multiPortData.ports.get(j);
          if (portData.traceId != 0)
          {
            Trace trace = circuitLoaders.getTraceLoader().create(portData.traceId);
            port.connect(trace);
          }

          loadPort(subcircuitSimulation, portData, port);
        }
      }
    }
  }

  private MultiPortData getMultiPortDataForSimulationId(SimulationMultiPortData simulationMultiPortData, long simulationId)
  {
    for (Map.Entry<Long, MultiPortData> entry : simulationMultiPortData.simulationMultiPortData.entrySet())
    {
      MultiPortData multiPortData = entry.getValue();
      long circuitSimulationId = entry.getKey();
      if (simulationId == circuitSimulationId)
      {
        return multiPortData;
      }
    }
    return null;
  }

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

  protected abstract T createComponentView(SubcircuitEditor subcircuitEditor);
}

