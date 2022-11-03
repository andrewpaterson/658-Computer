package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;

import java.awt.*;

import static net.logicim.ui.common.LineOverlap.*;
import static net.logicim.ui.common.Rotation.*;

public class TraceView
    extends ComponentView
{
  protected Int2D startPosition;
  protected Int2D endPosition;

  protected ConnectionView startJunction;
  protected ConnectionView endJunction;

  protected TraceNet trace;
  protected Rotation direction;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    this.startPosition = start.clone();
    this.endPosition = end.clone();
    this.trace = null;
    circuitEditor.add(this);
    direction = calculateDirection();
    startJunction = new ConnectionView(this);
    endJunction = new ConnectionView(this);
  }

  private Rotation calculateDirection()
  {
    return Rotation.calculateDirection(startPosition.x, startPosition.y, endPosition.x, endPosition.y);
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
    int x1 = viewport.transformGridToScreenSpaceX(startPosition.x);
    int y1 = viewport.transformGridToScreenSpaceY(startPosition.y);
    int x2 = viewport.transformGridToScreenSpaceX(endPosition.x);
    int y2 = viewport.transformGridToScreenSpaceY(endPosition.y);

    graphics.drawLine(x1, y1, x2, y2);
  }

  public void setTrace(TraceNet trace)
  {
    this.trace = trace;
  }

  public ConnectionView getJunctionInGrid(int x, int y)
  {
    if (startPosition.equals(x, y))
    {
      return startJunction;
    }
    if (endPosition.equals(x, y))
    {
      return endJunction;
    }

    boolean positionFallsOnTrace = isPositionOnTrace(x, y);

    if (positionFallsOnTrace)
    {
      return new HoverPortView(this, x, y);
    }
    else
    {
      return null;
    }

  }

  public ConnectionView getJunctionInGrid(Int2D gridPosition)
  {
    return getJunctionInGrid(gridPosition.x, gridPosition.y);
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
    if ((direction == North) &&
        (x == startPosition.x) &&
        ((y >= startPosition.y) && (y <= endPosition.y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == South) &&
        (x == startPosition.x) &&
        ((y >= endPosition.y) && (y <= startPosition.y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == West) &&
        (y == startPosition.y) &&
        ((x >= startPosition.x) && (x <= endPosition.x)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == Rotation.East) &&
        (y == startPosition.y) &&
        ((x >= endPosition.x) && (x <= startPosition.x)))
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
    center.set(startPosition);
    center.add(endPosition);
    center.divide(2);
  }

  public TraceOverlap getOverlap(Line line)
  {
    Rotation lineDirection = line.getDirection();
    if (lineDirection == Cannot)
    {
      return null;
    }
    else
    {
      if ((lineDirection == North || lineDirection == South) &&
          (direction == North || direction == South))
      {
        if (line.getStart().x == startPosition.x)
        {
          int lineMinY = line.getMinimumY();
          int lineMaxY = line.getMaximumY();
          int traceMinY = getMinimumY();
          int traceMaxY = getMaximumY();

          if (lineMaxY <= traceMinY)
          {
            return null;
          }
          if (lineMinY >= traceMaxY)
          {
            return null;
          }

          if ((lineMinY <= traceMinY) && (lineMaxY >= traceMaxY))
          {
            return new TraceOverlap(Fully, this);
          }
          if ((lineMinY > traceMinY) && (lineMaxY < traceMaxY))
          {
            return new TraceOverlap(Center, this);
          }

          if ((startPosition.y == lineMinY) || (startPosition.y == lineMaxY))
          {
            return new TraceOverlap(Start, this);
          }
          if ((endPosition.y == lineMinY) || (endPosition.y == lineMaxY))
          {
            return new TraceOverlap(End, this);
          }

          throw new SimulatorException("Could not determine line over trace overlap.");
        }
        else
        {
          return null;
        }
      }
      else if ((lineDirection == East || lineDirection == West) &&
               (direction == East || direction == West))
      {
        if (line.getStart().y == startPosition.y)
        {
          int lineMinX = line.getMinimumX();
          int lineMaxX = line.getMaximumY();
          int traceMinX = getMinimumX();
          int traceMaxX = getMaximumX();

          if (lineMaxX <= traceMinX)
          {
            return null;
          }
          if (lineMinX >= traceMaxX)
          {
            return null;
          }

          if ((lineMinX <= traceMinX) && (lineMaxX >= traceMaxX))
          {
            return new TraceOverlap(Fully, this);
          }
          if ((lineMinX > traceMinX) && (lineMaxX < traceMaxX))
          {
            return new TraceOverlap(Center, this);
          }

          if ((startPosition.x == lineMinX) || (startPosition.x == lineMaxX))
          {
            return new TraceOverlap(Start, this);
          }
          if ((endPosition.x == lineMinX) || (endPosition.x == lineMaxX))
          {
            return new TraceOverlap(End, this);
          }

          throw new SimulatorException("Could not determine line over trace overlap.");
        }
        else
        {
          return null;
        }
      }
      else
      {
        return null;
      }
    }
  }

  public int getMinimumY()
  {
    if (startPosition.y < endPosition.y)
    {
      return startPosition.y;
    }
    else
    {
      return endPosition.y;
    }
  }

  public int getMinimumX()
  {
    if (startPosition.x < endPosition.x)
    {
      return startPosition.x;
    }
    else
    {
      return endPosition.x;
    }
  }

  public int getMaximumY()
  {
    if (startPosition.y > endPosition.y)
    {
      return startPosition.y;
    }
    else
    {
      return endPosition.y;
    }
  }

  public int getMaximumX()
  {
    if (startPosition.x > endPosition.x)
    {
      return startPosition.x;
    }
    else
    {
      return endPosition.x;
    }
  }

  public ConnectionView getOpposite(Int2D gridPosition)
  {
    if (startPosition.equals(gridPosition))
    {
      return endJunction;
    }
    if (endPosition.equals(gridPosition))
    {
      return startJunction;
    }

    throw new SimulatorException("No opposite found.");
  }

  @Override
  public Int2D getGridPosition(ConnectionView connectionView)
  {
    if (startJunction == connectionView)
    {
      return startPosition;
    }
    else if (endJunction == connectionView)
    {
      return endPosition;
    }
    return null;
  }
}

