package net.logicim.ui.common.trace;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.awt.*;

public abstract class BaseTraceView
    extends ComponentView
{
  protected Line line;
  protected ConnectionView startConnection;
  protected ConnectionView endConnection;

  public BaseTraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    this.line = new Line(start, end);
    this.startConnection = circuitEditor.getOrAddConnection(start, this);
    this.endConnection = circuitEditor.getOrAddConnection(end, this);
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (line.getStart().equals(x, y))
    {
      return startConnection;
    }
    if (line.getEnd().equals(x, y))
    {
      return endConnection;
    }
    return null;
  }

  public ConnectionView getPotentialConnectionsInGrid(Int2D gridPosition)
  {
    return getPotentialConnectionsInGrid(gridPosition.x, gridPosition.y);
  }

  public ConnectionView getPotentialConnectionsInGrid(int x, int y)
  {
    ConnectionView connections = getConnectionsInGrid(x, y);
    if (connections != null)
    {
      return connections;
    }

    boolean positionFallsOnTrace = isPositionOnTrace(x, y);

    if (positionFallsOnTrace)
    {
      return new HoverConnectionView(this, x, y);
    }
    else
    {
      return null;
    }
  }

  private boolean isPositionOnTrace(Int2D gridPosition)
  {
    int x = gridPosition.x;
    int y = gridPosition.y;
    return isPositionOnTrace(x, y);
  }

  private boolean isPositionOnTrace(int x, int y)
  {
    return line.isPositionOn(x, y);
  }

  public boolean contains(Int2D gridPosition)
  {
    return isPositionOnTrace(gridPosition);
  }

  public boolean contains(int x, int y)
  {
    return isPositionOnTrace(x, y);
  }

  public void getCenter(Int2D center)
  {
    line.getCenter(center);
  }

  public LineOverlap getOverlap(Line otherLine)
  {
    return line.getOverlap(otherLine);
  }

  public ConnectionView getOpposite(Int2D gridPosition)
  {
    if (line.getStart().equals(gridPosition))
    {
      return endConnection;
    }
    if (line.getEnd().equals(gridPosition))
    {
      return startConnection;
    }

    throw new SimulatorException("No opposite found.");
  }

  public ConnectionView getOpposite(ConnectionView connection)
  {
    if (startConnection == connection)
    {
      return endConnection;
    }
    if (endConnection == connection)
    {
      return startConnection;
    }

    throw new SimulatorException("No opposite found.");
  }

  @Override
  public Int2D getGridPosition(ConnectionView connectionView)
  {
    if (startConnection == connectionView)
    {
      return line.getStart();
    }
    else if (endConnection == connectionView)
    {
      return line.getEnd();
    }
    return null;
  }

  public ConnectionView getConnectionsInGrid(Int2D gridPosition)
  {
    return getConnectionsInGrid(gridPosition.x, gridPosition.y);
  }

  @Override
  public Int2D getGridPosition()
  {
    return line.getStart();
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
    int x1 = viewport.transformGridToScreenSpaceX(line.getStart().x);
    int y1 = viewport.transformGridToScreenSpaceY(line.getStart().y);
    int x2 = viewport.transformGridToScreenSpaceX(line.getEnd().x);
    int y2 = viewport.transformGridToScreenSpaceY(line.getEnd().y);

    Color selectedColour = Colours.getInstance().getSelected();
    paintSelectionRectangle(graphics, viewport, x1, y1, selectedColour);
    paintSelectionRectangle(graphics, viewport, x2, y2, selectedColour);
  }

  @Override
  public void enable(Simulation simulation)
  {
  }

  @Override
  public void disable()
  {
  }

  @Override
  public void setPosition(int x, int y)
  {
    Int2D length = new Int2D(line.getEnd());
    length.subtract(line.getStart());
    line.getStart().set(x, y);
    line.getEnd().set(x, y);
    line.getEnd().add(length);
  }

  public Int2D getStartPosition()
  {
    return line.getStart();
  }

  public Int2D getEndPosition()
  {
    return line.getEnd();
  }

  public ConnectionView getStartConnection()
  {
    return startConnection;
  }

  public ConnectionView getEndConnection()
  {
    return endConnection;
  }

  public Rotation getDirection()
  {
    return line.getDirection();
  }

  public int getMinimumX()
  {
    return line.getMinimumX();
  }

  public int getMinimumY()
  {
    return line.getMinimumY();
  }

  public int getMaximumX()
  {
    return line.getMaximumX();
  }

  public int getMaximumY()
  {
    return line.getMaximumY();
  }

  public boolean isRemoved()
  {
    return startConnection == null || endConnection == null;
  }

  public Line getLine()
  {
    return line;
  }
}

