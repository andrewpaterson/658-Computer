package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.LogicPortData;
import net.logicim.data.port.common.MultiPortData;
import net.logicim.data.port.common.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.SubcircuitEditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentData<T extends ComponentView<?>>
    extends StaticData<T>
{
  protected List<MultiPortData> ports;

  public ComponentData()
  {
  }

  public ComponentData(Int2D position,
                       Rotation rotation,
                       String name,
                       List<MultiPortData> ports,
                       boolean selected)
  {
    super(name, position, rotation, null, null, selected);
    this.ports = ports;
  }

  public T createAndLoad(SubcircuitEditor subcircuitEditor,
                         TraceLoader traceLoader,
                         boolean fullLoad,
                         Simulation simulation,
                         Circuit circuit)
  {
    T componentView = create(subcircuitEditor.getSubcircuitView(),
                             circuit,
                             traceLoader,
                             fullLoad);
    if (fullLoad)
    {
      connectAndLoad(subcircuitEditor.getSubcircuitView(), simulation, traceLoader, componentView);
    }

    if (selected)
    {
      subcircuitEditor.select(componentView);
    }
    return componentView;
  }

  protected void connectAndLoad(SubcircuitView subcircuitView, Simulation simulation, TraceLoader traceLoader, T componentView)
  {
    componentView.createConnections(subcircuitView);
    componentView.enable(simulation);

    loadPorts(traceLoader, componentView, simulation);
  }

  protected void loadPorts(TraceLoader traceLoader, T componentView, Simulation simulation)
  {
    List<PortView> portViews = componentView.getPortViews();
    for (int i = 0; i < ports.size(); i++)
    {
      MultiPortData multiPortData = ports.get(i);
      PortView portView = portViews.get(i);

      List<? extends Port> ports = portView.getPorts();
      for (int j = 0; j < ports.size(); j++)
      {
        Port port = ports.get(j);
        if (j < multiPortData.ports.size())
        {
          PortData portData = multiPortData.ports.get(j);
          Trace trace = traceLoader.create(portData.traceId);
          port.connect(trace);

          loadPort(simulation, portData, port);
        }
      }
    }
  }

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

  protected abstract T create(SubcircuitView subcircuitView, Circuit circuit, TraceLoader traceLoader, boolean fullLoad);
}

