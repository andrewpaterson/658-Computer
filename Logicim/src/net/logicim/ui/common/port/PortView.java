package net.logicim.ui.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.port.common.*;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.domain.common.port.*;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PortView
{
  protected ComponentView<?> owner;

  protected Int2D relativePosition;
  protected String text;
  protected boolean inverting;
  protected boolean clockNotch;
  protected Float2D bubbleCenter;
  protected float bubbleDiameter;

  protected ConnectionView connection;

  protected PortViewGridCache gridCache;
  protected List<String> portNames;
  protected Map<InstanceCircuitSimulation, List<? extends Port>> simulationPorts;

  public PortView(ComponentView<?> componentView,
                  String portName,
                  Int2D relativePosition)
  {
    this(componentView,
         singlePort(portName),
         relativePosition);
  }

  public PortView(ComponentView<?> componentView,
                  List<String> portNames,
                  Int2D relativePosition)
  {
    this.owner = componentView;
    this.portNames = portNames;
    this.owner.addPortView(this);
    this.relativePosition = relativePosition;
    this.bubbleCenter = null;
    this.bubbleDiameter = 0.9f;
    this.connection = null;
    this.gridCache = new PortViewGridCache();
    this.simulationPorts = new LinkedHashMap<>();
  }

  public PortView setInverting(boolean inverting, Rotation facing)
  {
    this.inverting = inverting;
    Int2D portOffset = new Int2D(0, -1);
    facing.transform(portOffset);
    bubbleCenter = new Float2D(relativePosition);
    relativePosition.add(portOffset);
    Float2D bubbleOffset = new Float2D(0, -0.5f);
    facing.transform(bubbleOffset);
    bubbleCenter.add(bubbleOffset);

    gridCache.invalidate();
    return this;
  }

  public PortView setDrawClock(boolean drawClock)
  {
    this.clockNotch = drawClock;
    return this;
  }

  public PortView setText(String text)
  {
    this.text = text;
    return this;
  }

  protected void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(bubbleCenter,
                       relativePosition,
                       inverting,
                       owner.getRotation(),
                       owner.getPosition());
    }
  }

  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  public void updateBoundingBox(BoundingBox boundingBox)
  {
    if (inverting)
    {
      boundingBox.include(bubbleCenter, bubbleDiameter / 2);
    }
    boundingBox.include(relativePosition);
  }

  public Int2D getGridPosition()
  {
    updateGridCache();

    return gridCache.getPosition();
  }

  public boolean equals(int x, int y)
  {
    updateGridCache();

    return gridCache.getPosition().equals(x, y);
  }

  public ConnectionView getConnection()
  {
    return connection;
  }

  public void setConnection(ConnectionView connection)
  {
    this.connection = connection;
  }

  public Int2D getRelativePosition()
  {
    return relativePosition;
  }

  public void paint(Graphics2D graphics, Viewport viewport, InstanceCircuitSimulation circuit)
  {
    updateGridCache();

    Colours colours = Colours.getInstance();
    if (inverting)
    {
      Float2D gridBubbleCenter = gridCache.getBubbleCenter();
      if (gridBubbleCenter != null)
      {
        int x = viewport.transformGridToScreenSpaceX(gridBubbleCenter.x);
        int y = viewport.transformGridToScreenSpaceY(gridBubbleCenter.y);

        graphics.setStroke(viewport.getZoomableStroke());
        graphics.setColor(colours.getShapeBorder());
        int diameter = viewport.transformGridToScreenWidth(bubbleDiameter);
        graphics.drawOval(x - diameter / 2,
                          y - diameter / 2,
                          diameter,
                          diameter);
      }
    }

    Int2D gridPosition = gridCache.getPosition();
    if (gridPosition != null)
    {
      int x = viewport.transformGridToScreenSpaceX(gridPosition.x);
      int y = viewport.transformGridToScreenSpaceY(gridPosition.y);
      int lineWidth = (int) (viewport.getCircleRadius() * viewport.getConnectionSize());

      Color color = getPortColour(circuit);

      graphics.setColor(color);
      graphics.fillOval(x - lineWidth,
                        y - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  protected static List<String> singlePort(String portName)
  {
    if (portName != null)
    {
      ArrayList<String> ports = new ArrayList<>(1);
      ports.add(portName);
      return ports;
    }
    else
    {
      return null;
    }
  }

  public void disconnect()
  {
    for (Map.Entry<InstanceCircuitSimulation, List<? extends Port>> entry : simulationPorts.entrySet())
    {
      InstanceCircuitSimulation circuitSimulation = entry.getKey();
      List<? extends Port> ports = entry.getValue();
      if (ports != null)
      {
        for (Port port : ports)
        {
          port.disconnect(circuitSimulation.getSimulation());
        }
      }
    }
    connection = null;
  }

  public SimulationMultiPortData save()
  {
    SimulationMultiPortData simulationMultiPortData = new SimulationMultiPortData();

    for (Map.Entry<InstanceCircuitSimulation, List<? extends Port>> entry : simulationPorts.entrySet())
    {
      InstanceCircuitSimulation circuitSimulation = entry.getKey();
      List<? extends Port> ports = entry.getValue();
      ArrayList<PortData> portDatas = new ArrayList<>();
      for (Port port : ports)
      {
        if (port instanceof LogicPort)
        {
          portDatas.add(saveLogicPort((LogicPort) port));
        }
        else if (port instanceof PowerInPort)
        {
          portDatas.add(savePowerInPort((PowerInPort) port));
        }
        else if (port instanceof PowerOutPort)
        {
          portDatas.add(savePowerOutPort((PowerOutPort) port));
        }
        else if (port instanceof TracePort)
        {
          portDatas.add(saveTracePort((TracePort) port));
        }
        else
        {
          throw new SimulatorException("implement saving for [%s] ports.", port.getClass().getSimpleName());
        }
      }
      simulationMultiPortData.add(circuitSimulation.getId(), new MultiPortData(portDatas));
    }
    return simulationMultiPortData;
  }

  protected PowerOutPortData savePowerOutPort(PowerOutPort powerOutPort)
  {
    return new PowerOutPortData(powerOutPort.getTraceId());
  }

  protected TracePortData saveTracePort(TracePort powerOutPort)
  {
    return new TracePortData(powerOutPort.getTraceId());
  }

  protected PowerInPortData savePowerInPort(PowerInPort powerInPort)
  {
    return new PowerInPortData(powerInPort.getTraceId());
  }

  protected LogicPortData saveLogicPort(LogicPort logicPort)
  {
    PortOutputEvent portOutputEvent = logicPort.getOutput();
    LinkedList<PortEvent> portEvents = logicPort.getEvents();
    ArrayList<PortEventData<?>> eventDatas = new ArrayList<>(portEvents.size());
    PortOutputEventData<?> portOutputEventData = null;
    for (PortEvent event : portEvents)
    {
      PortEventData<?> portEventData = event.save();
      eventDatas.add(portEventData);
      if (portOutputEvent == event)
      {
        portOutputEventData = (PortOutputEventData<?>) portEventData;
      }
    }
    if (portOutputEventData == null)
    {
      if (portOutputEvent != null)
      {
        portOutputEventData = portOutputEvent.save();
      }
    }

    return new LogicPortData(eventDatas, portOutputEventData, logicPort.getTraceId());
  }

  protected Color getPortColour(InstanceCircuitSimulation circuit)
  {
    if (!allTracePorts(circuit))
    {
      List<? extends Port> ports = simulationPorts.get(circuit);
      if (ports != null)
      {
        return VoltageColour.getColorForPorts(Colours.getInstance(), ports, circuit.getTime());
      }
      else
      {
        return Colours.getInstance().getDisconnectedTrace();
      }
    }
    else
    {
      return VoltageColour.getColourForTraces(Colours.getInstance(), getTraces(circuit), circuit.getTime());
    }
  }

  private boolean allTracePorts(InstanceCircuitSimulation circuit)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    if (ports != null)
    {
      for (Port port : ports)
      {
        if (!(port instanceof TracePort))
        {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private List<Trace> getTraces(InstanceCircuitSimulation circuit)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    if (ports != null)
    {
      List<Trace> traces = new ArrayList<>(ports.size());
      for (Port port : ports)
      {
        Trace trace = port.getTrace();
        if (trace != null)
        {
          traces.add(trace);
        }
      }
      return traces;
    }
    else
    {
      return null;
    }
  }

  public void traceConnected(InstanceCircuitSimulation circuit)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    if (ports != null)
    {
      for (Port port : ports)
      {
        if (port.getTrace() != null)
        {
          port.traceConnected(circuit.getSimulation());
        }
      }
    }
  }

  public boolean containsPort(InstanceCircuitSimulation circuit, Port port)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    for (Port otherPort : ports)
    {
      if (otherPort == port)
      {
        return true;
      }
    }
    return false;
  }

  public List<? extends Port> getPorts(InstanceCircuitSimulation circuit)
  {
    return simulationPorts.get(circuit);
  }

  public int numberOfPorts()
  {
    return portNames.size();
  }

  public Port getPort(InstanceCircuitSimulation circuit, int index)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    if (ports != null)
    {
      if (index < ports.size())
      {
        return ports.get(index);
      }
      throw new SimulatorException("Cannot get port index [%s] for simulation [%s].  Index greater than ports size [%s].", index, circuit.getDescription(), ports.size());
    }
    else
    {
      throw new SimulatorException("Cannot get port index [%s] for simulation [%s].  Ports returned null.", index, circuit.getDescription());
    }
  }

  public String getText()
  {
    return text;
  }

  public TraceValue[] getValue(InstanceCircuitSimulation circuit, FamilyVoltageConfiguration voltageConfiguration, float vcc)
  {
    List<? extends Port> ports = simulationPorts.get(circuit);
    TraceValue[] traceValues = new TraceValue[ports.size()];
    for (int i = 0; i < ports.size(); i++)
    {
      Port port = ports.get(i);
      if (port != null && port.getTrace() != null)
      {
        float voltage = port.getTrace().getVoltage(circuit.getTime());
        traceValues[i] = voltageConfiguration.getValue(voltage, vcc);
      }
      else
      {
        traceValues[i] = TraceValue.Undriven;
      }
    }
    return traceValues;
  }

  public List<String> getPortNames()
  {
    return portNames;
  }

  public void addPorts(InstanceCircuitSimulation circuit, List<Port> ports)
  {
    if (simulationPorts.containsKey(circuit))
    {
      throw new SimulatorException("Ports have already been added for simulation [%s].", circuit.getDescription());
    }
    else
    {
      simulationPorts.put(circuit, ports);
    }
  }

  public void removePorts(InstanceCircuitSimulation circuit)
  {
    simulationPorts.remove(circuit);
  }
}

