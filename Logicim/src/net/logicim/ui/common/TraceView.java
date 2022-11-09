package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;

import java.awt.*;
import java.util.List;

import static net.logicim.ui.common.LineOverlap.*;
import static net.logicim.ui.common.Rotation.*;

public class TraceView
    extends ComponentView
{
  protected Int2D startPosition;
  protected Int2D endPosition;

  protected ConnectionView startConnection;
  protected ConnectionView endConnection;

  protected TraceNet trace;
  protected Rotation direction;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    this.startPosition = start.clone();
    this.endPosition = end.clone();
    this.trace = null;
    circuitEditor.add(this);
    direction = calculateDirection();
    startConnection = circuitEditor.getOrAddConnection(start, this);
    endConnection = circuitEditor.getOrAddConnection(end, this);
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

  public void connect(TraceNet trace)
  {
    this.trace = trace;
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (startPosition.equals(x, y))
    {
      return startConnection;
    }
    if (endPosition.equals(x, y))
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
      return new HoverPortView(this, x, y);
    }
    else
    {
      return null;
    }
  }

  public ConnectionView getConnectionsInGrid(Int2D gridPosition)
  {
    return getConnectionsInGrid(gridPosition.x, gridPosition.y);
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

  public LineOverlap getOverlap(Line line)
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
          return calculateOverlap(line.getMinimumY(),
                                  line.getMaximumY(),
                                  getMinimumY(),
                                  getMaximumY(),
                                  startPosition.y,
                                  endPosition.y);
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
          return calculateOverlap(line.getMinimumX(),
                                  line.getMaximumX(),
                                  getMinimumX(),
                                  getMaximumX(),
                                  startPosition.x,
                                  endPosition.x);
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

  private LineOverlap calculateOverlap(int lineMin, int lineMax, int traceMin, int traceMax, int traceStart, int traceEnd)
  {
    if (lineMax <= traceMin)
    {
      return null;
    }
    if (lineMin >= traceMax)
    {
      return null;
    }

    if ((lineMin <= traceMin) && (lineMax >= traceMax))
    {
      return Fully;  //The line fully overlaps the center of the trace.
    }
    if ((lineMin > traceMin) && (lineMax < traceMax))
    {
      return Center;  //The line only overlaps the center of the trace.
    }

    if ((traceStart >= lineMin) && (traceStart <= lineMax))
    {
      return Start;  //The line overlaps the start of the trace (but not the end).
    }
    if ((traceEnd >= lineMin) && (traceEnd <= lineMax))
    {
      return End;  //The line overlaps the end of the trace (but not the start).
    }

    throw new SimulatorException("Could not determine line over trace overlap.");
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
      return endConnection;
    }
    if (endPosition.equals(gridPosition))
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
      return startPosition;
    }
    else if (endConnection == connectionView)
    {
      return endPosition;
    }
    return null;
  }

  public TraceNet getTrace()
  {
    return trace;
  }

  public Int2D getStartPosition()
  {
    return startPosition;
  }

  public Int2D getEndPosition()
  {
    return endPosition;
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
    return direction;
  }

  public void disconnectTraceNet()
  {
    trace = null;
  }

  public void removed()
  {
    startConnection = null;
    endConnection = null;

    if (trace != null)
    {
      throw new SimulatorException("Trace must be disconnected before removal.");
    }
  }

  public boolean isStartStraight()
  {
    return areComponentsStraightTraces(startConnection.getConnectedComponents());
  }

  public boolean isEndStraight()
  {
    return areComponentsStraightTraces(endConnection.getConnectedComponents());
  }

  private boolean areComponentsStraightTraces(List<ComponentView> connectedComponents)
  {
    if (connectedComponents.size() == 1)
    {
        return true;
    }

    for (ComponentView connectedComponent : connectedComponents)
    {
      if (connectedComponent != this)
      {
        if (connectedComponent instanceof TraceView)
        {
          TraceView otherTrace = (TraceView) connectedComponent;
          if (!direction.isStraight(otherTrace.getDirection()))
          {
            return false;
          }
        }
        else
        {
          return false;
        }
      }
    }
    return true;
  }

  public TraceView getOtherTrace(List<ComponentView> connectedComponents)
  {
    TraceView otherTrace = null;
    if (connectedComponents.size() == 2)
    {
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent != this)
        {
          if (connectedComponent instanceof TraceView)
          {
            otherTrace = (TraceView) connectedComponent;
          }
        }
      }
    }
    return otherTrace;
  }
}

