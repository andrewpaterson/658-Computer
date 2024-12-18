package net.logicim.ui.common.wire;

import net.common.geometry.Line;
import net.common.type.Int2D;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
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

  public TraceView(SubcircuitView containingSubcircuitView, Line line)
  {
    this(containingSubcircuitView,
         line.getStart(),
         line.getEnd(),
         true);
  }

  public TraceView(SubcircuitView subcircuitView,
                   Int2D start,
                   Int2D end,
                   boolean addConnections)
  {
    this(subcircuitView,
         start,
         end,
         addConnections,
         nextId++);
  }

  public TraceView(SubcircuitView containingSubcircuitView,
                   Int2D start,
                   Int2D end,
                   boolean addConnections,
                   long id)
  {
    super(containingSubcircuitView, id);
    this.line = new Line(start, end);
    this.wireView = new WireViewComp();
    if (addConnections)
    {
      addConnections(start, end);
    }
    containingSubcircuitView.addTraceView(this);
  }

  protected void addConnections(Int2D start, Int2D end)
  {
    wireView.setStart(containingSubcircuitView.getOrAddConnectionView(start, this));
    wireView.setEnd(containingSubcircuitView.getOrAddConnectionView(end, this));
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

    boolean positionFallsOnTrace = line.isPositionOn(x, y);

    if (positionFallsOnTrace)
    {
      return new HoverConnectionView(this, x, y);
    }
    else
    {
      return null;
    }
  }

  public boolean contains(Int2D gridPosition)
  {
    return line.isPositionOn(gridPosition.x, gridPosition.y);
  }

  public boolean contains(int x, int y)
  {
    return line.isPositionOn(x, y);
  }

  public void getCenter(Int2D center)
  {
    line.getCenter(center);
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
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
  {
    graphics.setStroke(getTraceStroke(viewport));
    SubcircuitSimulation subcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
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
    return "Trace (" + getStartPosition() + ") to (" + getEndPosition() + ") length (" + getLength() + ")";
  }

  private int getLength()
  {
    return line.getLength();
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

  public void connectTraces(ViewPath viewPath, CircuitSimulation circuitSimulation, List<Trace> traces)
  {
    wireView.connectTraces(viewPath, circuitSimulation, traces);
  }

  public void disconnectViewAndDestroyComponents()
  {
    wireView.disconnectViewAndDestroyComponents();
  }

  public Traces getTraces(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    return wireView.getTraces(viewPath, circuitSimulation);
  }

  public void setLine(Int2D start, Int2D end)
  {
    line.set(start, end);
  }

  public LineOverlap touches(Line otherLine)
  {
    return line.touches(otherLine);
  }

  @Override
  public void destroyAllComponents()
  {
    wireView.destroyAllComponents();
  }

  @Override
  public void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    wireView.destroyComponent(viewPath, circuitSimulation);
  }

  public WireViewComp getWireViewComp()
  {
    return wireView;
  }

  @Override
  public String toString()
  {
    return getDescription();
  }
}

