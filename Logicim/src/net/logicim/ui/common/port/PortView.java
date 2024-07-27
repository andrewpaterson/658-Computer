package net.logicim.ui.common.port;

import net.common.SimulatorException;
import net.common.collection.linkedlist.LinkedList;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.port.common.*;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;
import net.logicim.domain.common.port.*;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.voltage.VoltageColour;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.List;
import java.util.*;

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
  protected Map<SubcircuitSimulation, List<? extends Port>> simulationPorts;

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

  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
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

      Color color = getPortColour(subcircuitSimulation);

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

  public void disconnectViewAndDestroyAllComponents()
  {
    destroyAllComponents();
    connection = null;
  }

  protected void destroyAllComponents()
  {
    for (Map.Entry<SubcircuitSimulation, List<? extends Port>> entry : simulationPorts.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      List<? extends Port> ports = entry.getValue();
      if (ports != null)
      {
        for (Port port : ports)
        {
          port.disconnect(subcircuitSimulation.getSimulation());
        }
      }
    }
    simulationPorts.clear();
  }

  public void destroyComponent(SubcircuitSimulation subcircuitSimulation)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
    if (ports != null)
    {
      for (Port port : ports)
      {
        port.disconnect(subcircuitSimulation.getSimulation());
      }
      simulationPorts.remove(subcircuitSimulation);
    }
  }

  public SimulationMultiPortData save()
  {
    SimulationMultiPortData simulationMultiPortData = new SimulationMultiPortData();

    for (Map.Entry<SubcircuitSimulation, List<? extends Port>> entry : simulationPorts.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
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
      simulationMultiPortData.add(subcircuitSimulation.getId(), new MultiPortData(portDatas));
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

  protected Color getPortColour(SubcircuitSimulation subcircuitSimulation)
  {
    if (!allTracePorts(subcircuitSimulation))
    {
      List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
      if (ports != null)
      {
        return VoltageColour.getColorForPorts(Colours.getInstance(), ports, subcircuitSimulation.getTime());
      }
      else
      {
        return Colours.getInstance().getDisconnectedTrace();
      }
    }
    else
    {
      return VoltageColour.getColourForTraces(Colours.getInstance(), getTraces(subcircuitSimulation), subcircuitSimulation.getTime());
    }
  }

  private boolean allTracePorts(SubcircuitSimulation subcircuitSimulation)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
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

  private List<Trace> getTraces(SubcircuitSimulation subcircuitSimulation)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
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

  public void traceConnected()
  {
    for (Map.Entry<SubcircuitSimulation, List<? extends Port>> entry : simulationPorts.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      List<? extends Port> ports = entry.getValue();
      if (ports != null)
      {
        for (Port port : ports)
        {
          if (port.getTrace() != null)
          {
            port.traceConnected(subcircuitSimulation.getSimulation());
          }
        }
      }
    }
  }

  public boolean containsPort(SubcircuitSimulation subcircuitSimulation, Port port)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
    for (Port otherPort : ports)
    {
      if (otherPort == port)
      {
        return true;
      }
    }
    return false;
  }

  public List<? extends Port> getPorts(SubcircuitSimulation subcircuitSimulation)
  {
    return simulationPorts.get(subcircuitSimulation);
  }

  public Map<SubcircuitSimulation, List<? extends Port>> getPorts()
  {
    return simulationPorts;
  }

  public int numberOfPorts()
  {
    return portNames.size();
  }

  public Port getPort(SubcircuitSimulation subcircuitSimulation, int index)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
    if (ports != null)
    {
      if (index < ports.size())
      {
        return ports.get(index);
      }
      throw new SimulatorException("Cannot get port index [%s] for simulation [%s].  Index greater than ports size [%s].", index, subcircuitSimulation.getDescription(), ports.size());
    }
    else
    {
      throw new SimulatorException("Cannot get port index [%s] for simulation [%s].  Ports returned null.", index, subcircuitSimulation.getDescription());
    }
  }

  public String getText()
  {
    return text;
  }

  public TraceValue[] getValue(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration voltageConfiguration, float vcc)
  {
    List<? extends Port> ports = simulationPorts.get(subcircuitSimulation);
    TraceValue[] traceValues = new TraceValue[ports.size()];
    for (int i = 0; i < ports.size(); i++)
    {
      Port port = ports.get(i);
      if (port != null && port.getTrace() != null)
      {
        float voltage = port.getTrace().getVoltage(subcircuitSimulation.getTime());
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

  public void addPorts(SubcircuitSimulation subcircuitSimulation, List<Port> ports)
  {
    if (simulationPorts.containsKey(subcircuitSimulation))
    {
      throw new SimulatorException("Ports have already been added for simulation [%s].", subcircuitSimulation.getDescription());
    }
    else
    {
      simulationPorts.put(subcircuitSimulation, ports);
    }
  }

  public String getDescription()
  {
    if (text != null)
    {
      return text + " on " + owner.getDescription();
    }
    else
    {
      return "on " + owner.getDescription();
    }
  }

  public Set<SubcircuitSimulation> getPortSubcircuitSimulations()
  {
    return new LinkedHashSet<>(simulationPorts.keySet());
  }
}

