package net.logicim.ui.common.wire;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.View;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class TraceView
    extends View
    implements WireView
{
  protected WireViewComp wireView;

  protected Line line;

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
    wireView = new WireViewComp();
    if (addConnections)
    {
      wireView.setStart(subcircuitView.getOrAddConnectionView(start, this));
      wireView.setEnd(subcircuitView.getOrAddConnectionView(end, this));
    }
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
    return wireView.getStartConnection();
  }

  public ConnectionView getEndConnection()
  {
    return wireView.getEndConnection();
  }

  public boolean hasConnections()
  {
    return wireView.hasConnections();
  }

  @Override
  public List<ConnectionView> getConnectionViews()
  {
    return wireView.getConnections();
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
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    graphics.setStroke(getTraceStroke(viewport));
    Color color = getTraceColour(subcircuitSimulation);
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
    return wireView.getTraceStroke(viewport);
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

  protected Color getTraceColour(SubcircuitSimulation subcircuitSimulation)
  {
    return wireView.getTraceColour(subcircuitSimulation);
  }

  public TraceData save(boolean selected)
  {
    Map<Long, long[]> simulationTraces = wireView.save();

    return new TraceData(simulationTraces,
                         getStartPosition(),
                         getEndPosition(),
                         id,
                         enabled,
                         selected);
  }

  public void connectTraces(SubcircuitSimulation subcircuitSimulation, List<Trace> traces)
  {
    wireView.connectTraces(subcircuitSimulation, traces);
  }

  public void disconnect()
  {
    wireView.disconnect();
  }

  @Override
  public void clearTraces(SubcircuitSimulation subcircuitSimulation)
  {
    wireView.clearTraces(subcircuitSimulation);
  }

  public List<Trace> getTraces(SubcircuitSimulation subcircuitSimulation)
  {
    return wireView.getTraces(subcircuitSimulation);
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

  public void destroyComponent(CircuitSimulation circuitSimulation)
  {
    wireView.destroyComponent(circuitSimulation);
  }
}

