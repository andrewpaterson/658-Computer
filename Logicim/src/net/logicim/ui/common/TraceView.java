package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;

import java.awt.*;

public class TraceView
{
  protected JunctionView start;
  protected JunctionView end;

  protected TransientJunctionView temp;

  protected TraceNet trace;
  protected Rotation direction;

  public TraceView(CircuitEditor circuitEditor, JunctionView start, JunctionView end)
  {
    this.start = start;
    this.end = end;
    this.trace = null;
    circuitEditor.add(this);
    direction = calculateDirection();
    temp = new TransientJunctionView();
  }

  private Rotation calculateDirection()
  {
    if (start.getPosition().x == end.getPosition().x)
    {
      if (start.getPosition().y < end.getPosition().y)
      {
        return Rotation.NORTH;
      }
      if (start.getPosition().y > end.getPosition().y)
      {
        return Rotation.SOUTH;
      }
    }
    if (start.getPosition().y == end.getPosition().y)
    {
      if (start.getPosition().x < end.getPosition().x)
      {
        return Rotation.WEST;
      }
      if (start.getPosition().x > end.getPosition().x)
      {
        return Rotation.EAST;
      }
    }

    return Rotation.CANNOT;
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    Color color;
    if (trace == null)
    {
      color = viewport.getColours().getDisconnectedTrace();
    }
    else
    {
      float voltage = trace.getVoltage();
      color = VoltageColour.getColorForVoltage(viewport.getColours(), voltage);
    }
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(start.getPosition().x);
    int y1 = viewport.transformGridToScreenSpaceY(start.getPosition().y);
    int x2 = viewport.transformGridToScreenSpaceX(end.getPosition().x);
    int y2 = viewport.transformGridToScreenSpaceY(end.getPosition().y);

    graphics.drawLine(x1, y1, x2, y2);
  }

  public JunctionView getStart()
  {
    return start;
  }

  public JunctionView getEnd()
  {
    return end;
  }

  public void setTrace(TraceNet trace)
  {
    this.trace = trace;
  }

  public ConnectionView getJunctionInGrid(int x, int y)
  {
    if (start.equals(x, y))
    {
      return start;
    }
    if (end.equals(x, y))
    {
      return end;
    }

    boolean positionFallsOnTrace = isPositionOnTrace(x, y);

    if (positionFallsOnTrace)
    {
      temp.set(x, y);
      return temp;
    }
    else
    {
      return null;
    }

  }

  public BaseJunctionView getJunctionInGrid(Int2D gridPosition)
  {
    if (start.equals(gridPosition))
    {
      return start;
    }
    if (end.equals(gridPosition))
    {
      return end;
    }

    boolean positionFallsOnTrace = isPositionOnTrace(gridPosition);

    if (positionFallsOnTrace)
    {
      temp.set(gridPosition);
      return temp;
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
    boolean positionFallsOnTrace = false;
    if ((direction == Rotation.NORTH) &&
        (x == start.getPosition().x) &&
        ((y >= start.getPosition().y) && (y <= end.getPosition().y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == Rotation.SOUTH) &&
        (x == start.getPosition().x) &&
        ((y >= end.getPosition().y) && (y <= start.getPosition().y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == Rotation.WEST) &&
        (y == start.getPosition().y) &&
        ((x >= start.getPosition().x) && (x <= end.getPosition().x)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == Rotation.EAST) &&
        (y == start.getPosition().y) &&
        ((x >= end.getPosition().x) && (x <= start.getPosition().x)))
    {
      positionFallsOnTrace = true;
    }
    return positionFallsOnTrace;
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
    center.set(start.getPosition());
    center.add(end.getPosition());
    center.divide(2);
  }
}

