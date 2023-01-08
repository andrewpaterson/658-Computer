package net.logicim.ui.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.port.*;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.trace.Trace;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PortView
{
  protected DiscreteView<?> owner;

  protected Int2D positionRelativeToIC;
  protected boolean inverting;
  protected String text;
  protected boolean overline;
  protected Float2D bubbleCenter;
  protected float bubbleDiameter;

  protected ConnectionView connection;

  protected PortViewGridCache gridCache;
  protected List<Port> ports;

  public PortView(DiscreteView<?> discreteView, Port port, Int2D positionRelativeToIC)
  {
    this(discreteView, singlePort(port), positionRelativeToIC);
  }

  public PortView(DiscreteView<?> discreteView, List<Port> ports, Int2D positionRelativeToIC)
  {
    this.owner = discreteView;
    this.owner.addPortView(this);
    this.positionRelativeToIC = positionRelativeToIC;
    this.bubbleCenter = null;
    this.bubbleDiameter = 0.9f;
    connection = null;
    gridCache = new PortViewGridCache();
    this.ports = ports;
  }

  public PortView setInverting(boolean inverting, Rotation facing)
  {
    this.inverting = inverting;
    Int2D portOffset = new Int2D(0, -1);
    facing.transform(portOffset);
    bubbleCenter = new Float2D(positionRelativeToIC);
    positionRelativeToIC.add(portOffset);
    Float2D bubbleOffset = new Float2D(0, -0.5f);
    facing.transform(bubbleOffset);
    bubbleCenter.add(bubbleOffset);

    gridCache.invalidate();
    return this;
  }

  public PortView setDrawBar(boolean drawBar)
  {
    this.overline = drawBar;
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
                       positionRelativeToIC,
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
    boundingBox.include(positionRelativeToIC);
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

  public Int2D getPosition()
  {
    return positionRelativeToIC;
  }

  public void paint(Graphics2D graphics, Viewport viewport, long time)
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

      Color color = getPortColour(time, colours);

      graphics.setColor(color);
      graphics.fillOval(x - lineWidth,
                        y - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  protected static List<Port> singlePort(Port port)
  {
    ArrayList<Port> ports = new ArrayList<>(1);
    ports.add(port);
    return ports;
  }

  public void connectTraceNet(List<Trace> traces, Simulation simulation)
  {
    for (int i = 0; i < ports.size(); i++)
    {
      Port port = ports.get(i);
      Trace trace = traces.get(i);
      port.disconnect(simulation);
      port.connect(trace);
    }
  }

  public void disconnect(Simulation simulation)
  {
    for (Port port : ports)
    {
      port.disconnect(simulation);
    }
  }

  public MultiPortData save()
  {
    ArrayList<PortData> portDatas = new ArrayList<>();
    for (Port port : this.ports)
    {
      if (port.isLogicPort())
      {
        portDatas.add(saveLogicPort((LogicPort) port));
      }
      else if (port.isPowerIn())
      {
        portDatas.add(savePowerInPort((PowerInPort) port));
      }
      else if (port.isPowerOut())
      {
        portDatas.add(savePowerOutPort((PowerOutPort) port));
      }
      else
      {
        throw new SimulatorException("implement saving for non-logic ports.");
      }
    }
    return new MultiPortData(portDatas);
  }

  protected PowerOutPortData savePowerOutPort(PowerOutPort powerOutPort)
  {
    return new PowerOutPortData(powerOutPort.getTraceId());
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

  protected Color getPortColour(long time, Colours colours)
  {
    Color colour = null;
    for (Port port : ports)
    {
      Color portColour = VoltageColour.getColorForPort(Colours.getInstance(), port, time);
      if (colour == null)
      {
        colour = portColour;
      }
      else
      {
        if (colour.getRGB() != portColour.getRGB())
        {
          return Colours.getInstance().getDifferingBusTrace();
        }
      }
    }
    return colour;
  }

  public void traceConnected(Simulation simulation)
  {
    for (Port port : ports)
    {
      port.traceConnected(simulation);
    }
  }

  public boolean containsPort(Port port)
  {
    for (Port otherPort : ports)
    {
      if (otherPort == port)
      {
        return true;
      }
    }
    return false;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  public int numberOfPorts()
  {
    return ports.size();
  }
}
