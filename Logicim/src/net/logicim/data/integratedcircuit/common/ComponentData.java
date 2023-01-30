package net.logicim.data.integratedcircuit.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.port.LogicPortData;
import net.logicim.data.port.MultiPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.CircuitEditor;

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

  public void createAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    T componentView = create(circuitEditor, traceLoader);
    connectAndLoad(circuitEditor, traceLoader, componentView);

    if (selected)
    {
      circuitEditor.select(componentView);
    }
  }

  protected void connectAndLoad(CircuitEditor circuitEditor, TraceLoader traceLoader, T componentView)
  {
    componentView.createConnections(circuitEditor);
    componentView.enable(circuitEditor.getSimulation());

    loadPorts(circuitEditor, traceLoader, componentView);
  }

  protected void loadPorts(CircuitEditor circuitEditor, TraceLoader traceLoader, T componentView)
  {
    List<PortView> portViews = componentView.getPorts();
    for (int i = 0; i < ports.size(); i++)
    {
      MultiPortData multiPortData = ports.get(i);
      PortView portView = portViews.get(i);

      List<? extends Port> ports = portView.getPorts();
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

  protected abstract T create(CircuitEditor circuitEditor, TraceLoader traceLoader);
}

