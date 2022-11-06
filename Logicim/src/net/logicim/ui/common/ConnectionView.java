package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionView
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
      graphics.setColor(viewport.getColours().getPortHover());
      graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
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
}

