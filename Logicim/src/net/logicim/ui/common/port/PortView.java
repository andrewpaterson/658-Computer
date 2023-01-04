package net.logicim.ui.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.port.LogicPortData;
import net.logicim.data.port.PortData;
import net.logicim.data.port.PowerInPortData;
import net.logicim.data.port.PowerOutPortData;
import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerInPort;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.PortOutputEvent;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;

public class PortView
{
  protected DiscreteView<?> owner;
  protected Port port;
  protected Int2D positionRelativeToIC;

  protected boolean inverting;
  protected String text;
  protected boolean overline;
  protected Float2D bubbleCenter;
  protected float bubbleDiameter;

  protected ConnectionView connection;

  protected PortViewGridCache gridCache;

  public PortView(DiscreteView<?> integratedCircuit, Port port, Int2D positionRelativeToIC)
  {
    this.owner = integratedCircuit;
    this.port = port;
    this.positionRelativeToIC = positionRelativeToIC;
    this.owner.addPortView(this);

    this.bubbleCenter = null;
    this.bubbleDiameter = 0.9f;

    connection = null;

    gridCache = new PortViewGridCache();
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

  public Port getPort()
  {
    return port;
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

        graphics.setStroke(viewport.getStroke());
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

      Port port = getPort();
      Color color = VoltageColour.getColorForPort(colours, port, time);

      graphics.setColor(color);
      graphics.fillOval(x - lineWidth,
                        y - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  private void updateGridCache()
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

  public void connectTraceNet(TraceNet trace, Simulation simulation)
  {
    port.disconnect(simulation);
    port.connect(trace);
  }

  public void disconnectTraceNet(Simulation simulation)
  {
    port.disconnect(simulation);
  }

  public PortData save()
  {
    if (port.isLogicPort())
    {
      LogicPort port = (LogicPort) this.port;
      PortOutputEvent portOutputEvent = port.getOutput();
      LinkedList<PortEvent> portEvents = port.getEvents();
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

      return new LogicPortData(eventDatas, portOutputEventData, port.getTraceId());
    }
    else if (port.isPowerIn())
    {
      PowerInPort port = (PowerInPort) this.port;
      return new PowerInPortData(port.getTraceId());
    }
    else if (port.isPowerOut())
    {
      PowerOutPort port = (PowerOutPort) this.port;
      return new PowerOutPortData(port.getTraceId());
    }
    else
    {
      throw new SimulatorException("implement saving for non-logic ports.");
    }
  }

  public Int2D getPosition()
  {
    return positionRelativeToIC;
  }
}

