package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;

import java.awt.*;
import java.util.List;

public class TraceView
    extends ComponentView
{
  protected Line line;

  protected ConnectionView startConnection;
  protected ConnectionView endConnection;

  protected TraceNet trace;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    line = new Line(start, end);
    this.trace = null;
    circuitEditor.add(this);
    startConnection = circuitEditor.getOrAddConnection(start, this);
    endConnection = circuitEditor.getOrAddConnection(end, this);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    Color color = VoltageColour.getColorForTrace(viewport.getColours(), trace);
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(line.getStart().x);
    int y1 = viewport.transformGridToScreenSpaceY(line.getStart().y);
    int x2 = viewport.transformGridToScreenSpaceX(line.getEnd().x);
    int y2 = viewport.transformGridToScreenSpaceY(line.getEnd().y);

    graphics.drawLine(x1, y1, x2, y2);

    int lineWidth = (int) (viewport.getCircleRadius() * viewport.getConnectionSize());

    if (!startConnection.isNonJunctionTracesOnly())
    {
      graphics.fillOval(x1 - lineWidth,
                        y1 - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
    if (!endConnection.isNonJunctionTracesOnly())
    {
      graphics.fillOval(x2 - lineWidth,
                        y2 - lineWidth,
                        lineWidth * 2,
                        lineWidth * 2);
    }
  }

  public void connect(TraceNet trace)
  {
    this.trace = trace;
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

  public TraceNet getTrace()
  {
    return trace;
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

    Rotation direction = line.getDirection();
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
}

