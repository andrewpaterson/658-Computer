package net.logicim.ui.common;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionView
    implements Comparable<ConnectionView>
{
  protected List<View> connectedComponents;
  protected Int2D gridPosition;

  public ConnectionView(View parentView, Int2D gridPosition)
  {
    this(parentView, gridPosition.x, gridPosition.y);
  }

  public ConnectionView(View parentView, int x, int y)
  {
    connectedComponents = new ArrayList<>();
    connectedComponents.add(parentView);
    this.gridPosition = new Int2D(x, y);
  }

  public static String toPositionString(ConnectionView connectionView)
  {
    if (connectionView != null)
    {
      return Int2D.toString(connectionView.getGridPosition());
    }
    else
    {
      return "null";
    }
  }

  public List<View> getConnectedComponents()
  {
    return connectedComponents;
  }

  public Int2D getGridPosition()
  {
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

  public void add(View view)
  {
    if (!connectedComponents.contains(view))
    {
      connectedComponents.add(view);
    }
    else
    {
      throw new SimulatorException("Component has already been added to connection.");
    }
  }

  public void remove(View view)
  {
    boolean removed = connectedComponents.remove(view);
    if (!removed)
    {
      throw new SimulatorException("Could not remove %s from connections.", view.toIdentifierString());
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
    Int2D position = getGridPosition();
    String positionString = "?, ?";
    if (position != null)
    {
      positionString = position.toString();
    }
    String connectedComponentsSizeString = "?";
    if (connectedComponents != null)
    {
      connectedComponentsSizeString = Integer.toString(connectedComponents.size());
    }
    return positionString + " (" + connectedComponentsSizeString + ")";
  }

  public boolean isNonJunctionTracesOnly()
  {
    if ((connectedComponents.size() > 0) && (connectedComponents.size() <= 2))
    {
      int traceCount = 0;
      for (View connectedComponent : connectedComponents)
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

  public boolean isConnectedComponentsEmpty()
  {
    return connectedComponents.isEmpty();
  }
}

