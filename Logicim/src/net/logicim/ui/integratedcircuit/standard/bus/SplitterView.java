package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.splitter.SplitterData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.trace.Trace;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.NonTraceView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SplitterView
    extends NonTraceView<SplitterProperties>
{
  protected List<Trace> traces;
  protected ConnectionView startConnection;
  protected List<ConnectionView> endConnections;

  protected List<Int2D> ends;

  public SplitterView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, SplitterProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    this.position = position;
    this.rotation = rotation;
    this.properties = properties;

    this.endConnections = createEndConnections(properties);
    this.ends = createEnds(properties);

    this.boundingBox = new BoundingBox();
    this.selectionBox = new BoundingBox();
    this.traces = new ArrayList<>();

    circuitEditor.addSplitterView(this);
    calculateEnds();
  }

  protected List<ConnectionView> createEndConnections(SplitterProperties properties)
  {
    ArrayList<ConnectionView> endConnections = new ArrayList<>(properties.outputCount);
    for (int i = 0; i < properties.outputCount; i++)
    {
      endConnections.add(new ConnectionView(this));
    }
    return endConnections;
  }

  protected List<Int2D> createEnds(SplitterProperties properties)
  {
    ArrayList<Int2D> ends = new ArrayList<>(properties.outputCount);
    for (int i = 0; i < properties.outputCount; i++)
    {
      ends.add(new Int2D(position));
    }
    return ends;
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (position.equals(x, y))
    {
      return startConnection;
    }
    for (int i = 0; i < ends.size(); i++)
    {
      Int2D end = ends.get(i);
      if (end.equals(x, y))
      {
        return endConnections.get(i);
      }
    }
    return null;
  }

  @Override
  public Int2D getConnectionPosition(ConnectionView connectionView)
  {
    if (startConnection == connectionView)
    {
      return position;
    }
    for (int i = 0; i < endConnections.size(); i++)
    {
      if (endConnections.get(i) == connectionView)
      {
        return ends.get(i);
      }
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
  }

  @Override
  public String getType()
  {
    return "Splitter";
  }

  @Override
  public String getDescription()
  {
    return "Splitter (" + position + ") width [" + properties.outputCount + "]";
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
    super.setPosition(x,y);
    calculateEnds();
  }

  private void calculateEnds()
  {
    Int2D position = new Int2D(this.position);
    position.add(3, -properties.outputOffset);
    for (Int2D end : ends)
    {
      rotation.rotate(end, position);
      position.add(0, properties.spacing);
    }
  }

  public SplitterData save(boolean selected)
  {
    throw new SimulatorException("Not yet implemented");
  }

  @Override
  public void clampProperties()
  {

  }

  @Override
  public boolean isEnabled()
  {
    return false;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {

  }

  @Override
  protected void updateBoundingBoxFromConnections(BoundingBox boundingBox)
  {

  }
}

