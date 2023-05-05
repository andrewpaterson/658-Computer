package net.logicim.ui.common.wire;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TraceView
    extends View
    implements WireView
{
  protected Line line;
  protected List<ConnectionView> connections;
  protected List<Trace> traces;

  public TraceView(SubcircuitView subcircuitView, Line line)
  {
    this(subcircuitView, line.getStart(), line.getEnd());
  }

  public TraceView(SubcircuitView subcircuitView, Int2D start, Int2D end)
  {
    this(subcircuitView, start, end, true);
  }

  public TraceView(SubcircuitView subcircuitView, Int2D start, Int2D end, boolean addConnections)
  {
    super();
    this.line = new Line(start, end);
    connections = new ArrayList<>(2);
    if (addConnections)
    {
      connections.add(subcircuitView.getOrAddConnectionView(start, this));
      connections.add(subcircuitView.getOrAddConnectionView(end, this));
    }
    else
    {
      connections.add(null);
      connections.add(null);
    }
    this.traces = new ArrayList<>();
    subcircuitView.addTraceView(this);
  }

  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (line.getStart().equals(x, y))
    {
      return getStartConnection();
    }
    if (line.getEnd().equals(x, y))
    {
      return getEndConnection();
    }
    return null;
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

  public LineOverlap getOverlap(Line otherLine, boolean endInclusive)
  {
    return line.getOverlap(otherLine, endInclusive);
  }

  public ConnectionView getOpposite(ConnectionView connection)
  {
    if (getStartConnection() == connection)
    {
      return getEndConnection();
    }
    if (getEndConnection() == connection)
    {
      return getStartConnection();
    }

    //throw new SimulatorException("No opposite found.");
    return null;
  }

  @Override
  public Int2D getPosition()
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
    return connections.get(0);
  }

  public ConnectionView getEndConnection()
  {
    return connections.get(1);
  }

  public boolean hasConnections()
  {
    return getStartConnection() != null && getEndConnection() != null;
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (ConnectionView connection : connections)
    {
      if (connection != null)
      {
        connectionViews.add(connection);
      }
    }
    return connectionViews;
  }

  @Override
  public TraceView getView()
  {
    return this;
  }

  public Line getLine()
  {
    return line;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation)
  {
    graphics.setStroke(getTraceStroke(viewport));
    Color color = getTraceColour(simulation);
    graphics.setColor(color);
    int x1 = viewport.transformGridToScreenSpaceX(line.getStart().x);
    int y1 = viewport.transformGridToScreenSpaceY(line.getStart().y);
    int x2 = viewport.transformGridToScreenSpaceX(line.getEnd().x);
    int y2 = viewport.transformGridToScreenSpaceY(line.getEnd().y);

    graphics.drawLine(x1, y1, x2, y2);

    int lineWidth = (int) (viewport.getCircleRadius() * viewport.getConnectionSize());

    if (hasConnections())
    {
      if (!getStartConnection().isNonJunctionTracesOnly())
      {
        graphics.fillOval(x1 - lineWidth,
                          y1 - lineWidth,
                          lineWidth * 2,
                          lineWidth * 2);
      }
      if (!getEndConnection().isNonJunctionTracesOnly())
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
    return null;
  }

  @Override
  public String getType()
  {
    return "Trace";
  }

  @Override
  public String getDescription()
  {
    return "Trace (" + getStartPosition() + ") to (" + getEndPosition() + ")";
  }

  protected Color getTraceColour(CircuitSimulation simulation)
  {
    if (simulation != null)
    {
      return VoltageColour.getColourForTraces(Colours.getInstance(), traces, simulation.getTime());
    }
    else
    {
      return Colours.getInstance().getDisconnectedTrace();
    }
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
                         id,
                         enabled,
                         selected);
  }

  public void connectTraces(List<Trace> traces)
  {
    this.traces = traces;
  }

  public void disconnect()
  {
    for (int i = 0; i < connections.size(); i++)
    {
      connections.set(i, null);
    }
    clearTraces();
  }

  @Override
  public void clearTraces()
  {
    traces.clear();
  }

  public List<Trace> getTraces()
  {
    return traces;
  }

  public void setLine(Int2D start, Int2D end)
  {
    line.set(start, end);
  }

  public LineOverlap touches(Line line)
  {
    LineOverlap overlap = getOverlap(line, true);
    if (overlap != LineOverlap.None)
    {
      return overlap;
    }
    if (isPositionOnTrace(line.getStart()))
    {
      return LineOverlap.Orthogonal;
    }
    if (isPositionOnTrace(line.getEnd()))
    {
      return LineOverlap.Orthogonal;
    }
    return LineOverlap.None;
  }
}

