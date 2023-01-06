package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.trace.TraceView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionView
    implements Comparable<ConnectionView>
{
  protected List<ComponentView> connectedComponents;

  public ConnectionView()
  {
    connectedComponents = new ArrayList<>();
  }

  public ConnectionView(ComponentView parentView)
  {
    this();
    connectedComponents.add(parentView);
  }

  public List<ComponentView> getConnectedComponents()
  {
    return connectedComponents;
  }

  public Int2D getGridPosition()
  {
    Int2D gridPosition = null;
    for (ComponentView connectedComponent : connectedComponents)
    {
      Int2D position = connectedComponent.getGridPosition(this);
      if (gridPosition == null)
      {
        gridPosition = position;
      }
      else if (!gridPosition.equals(position))
      {
        throw new SimulatorException("Expected every position in a connection to be equal.");
      }
    }
    return gridPosition;
  }

  public void paintHoverPort(Graphics2D graphics, Viewport viewport)
  {
    try
    {
      Int2D gridPosition = getGridPosition();
      int x = viewport.transformGridToScreenSpaceX(gridPosition.x);
      int y = viewport.transformGridToScreenSpaceY(gridPosition.y);
      int radius = (int) (viewport.getCircleRadius() * 5);
      graphics.setColor(Colours.getInstance().getPortHover());
      graphics.setStroke(viewport.getZoomableStroke());
      graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
    catch (RuntimeException ignored)
    {
    }
  }

  public void add(ComponentView componentView)
  {
    if (!connectedComponents.contains(componentView))
    {
      connectedComponents.add(componentView);
    }
    else
    {
      throw new SimulatorException("Component has already been added to connection.");
    }
  }

  public boolean isConcrete()
  {
    return true;
  }

  public void remove(ComponentView traceView)
  {
    boolean removed = connectedComponents.remove(traceView);
    if (!removed)
    {
      throw new SimulatorException("Could not remove component from connections.");
    }
  }

  @Override
  public int compareTo(ConnectionView other)
  {
    Int2D position = getGridPosition();
    Int2D otherPosition = other.getGridPosition();
    int dx = otherPosition.x - position.x;
    int dy = otherPosition.y - position.y;
    if (dx < 0)
    {
      dx *= -1;
    }
    if (dy < 0)
    {
      dy *= -1;
    }

    if (dx < dy)
    {
      return Integer.compare(position.y, otherPosition.y);
    }
    else if (dy < dx)
    {
      return Integer.compare(position.x, otherPosition.x);
    }
    return 0;
  }

  @Override
  public String toString()
  {
    return getGridPosition().toString() + " (" + connectedComponents.size() + ")";
  }

  public boolean isNonJunctionTracesOnly()
  {
    if ((connectedComponents.size() > 0) && (connectedComponents.size() <= 2))
    {
      int traceCount = 0;
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          traceCount++;
        }
        else
        {
          return false;
        }
      }
      if ((traceCount > 0) && (traceCount <= 2))
      {
        return true;
      }
    }
    return false;
  }
}

