package net.logicim.ui.common.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TraceView
    extends View
    implements WireView
{
  protected Line line;
  protected ConnectionView startConnection;
  protected ConnectionView endConnection;
  protected List<Trace> traces;

  public TraceView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    super();
    this.line = new Line(start, end);
    this.startConnection = circuitEditor.getOrAddConnection(start, this);
    this.endConnection = circuitEditor.getOrAddConnection(end, this);
    this.traces = new ArrayList<>();
    circuitEditor.addTraceView(this);
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
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
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
  public Int2D getPosition()
  {
    return line.getStart();
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

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    graphics.setStroke(getTraceStroke(viewport));
    Color color = getTraceColour(time);
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(line.getStart().x);
    int y1 = viewport.transformGridToScreenSpaceY(line.getStart().y);
    int x2 = viewport.transformGridToScreenSpaceX(line.getEnd().x);
    int y2 = viewport.transformGridToScreenSpaceY(line.getEnd().y);

    graphics.drawLine(x1, y1, x2, y2);

    int lineWidth = (int) (viewport.getCircleRadius() * viewport.getConnectionSize());

    if (!isRemoved())
    {
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
  }

  protected Stroke getTraceStroke(Viewport viewport)
  {
    if (traces.size() == 1)
    {
      return viewport.getZoomableStroke();
    }
    else
    {
      return viewport.getZoomableStroke(3.0f);
    }
  }

  @Override
  public String getName()
  {
    return "Trace";
  }

  @Override
  public String getDescription()
  {
    return "Trace (" + getStartPosition() + ") to (" + getEndPosition() + ")";
  }

  public void removed()
  {
    startConnection = null;
    endConnection = null;

    if (!traces.isEmpty())
    {
      throw new SimulatorException("Trace must be disconnected before removal.");
    }
  }

  protected Color getTraceColour(long time)
  {
    return VoltageColour.getColourForTraces(Colours.getInstance(), traces, time);
  }

  public TraceView getOtherTraceView(List<View> connectedComponents)
  {
    TraceView otherTrace = null;
    if (connectedComponents.size() == 2)
    {
      for (View connectedComponent : connectedComponents)
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

  public TraceData save(boolean selected)
  {
    long[] ids = new long[traces.size()];
    for (int i = 0; i < traces.size(); i++)
    {
      Trace trace = traces.get(i);
      ids[i] = Trace.getId(trace);
    }

    return new TraceData(ids,
                         getStartPosition(),
                         getEndPosition(),
                         selected);
  }

  public void connectTraces(List<Trace> traces)
  {
    this.traces = traces;
  }

  public void disconnectTraces()
  {
    traces = new ArrayList<>();
  }

  public List<Trace> getTraces()
  {
    return traces;
  }
}

