package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TunnelData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TunnelView
    extends StaticView
    implements WireView
{
  protected Set<TunnelView> tunnels;
  protected ConnectionView connection;
  protected String name;
  protected Int2D position;
  protected List<Trace> traces;

  public TunnelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, String name)
  {
    super(circuitEditor, position, rotation);
    this.connection = circuitEditor.getOrAddConnection(position, this);
    this.position = position.clone();
    this.traces = new ArrayList<>();
    this.name = name;
    this.tunnels = circuitEditor.addTunnel(this);
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (position.equals(x, y))
    {
      return connection;
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    if (this.connection == connectionView)
    {
      return position;
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return "Tunnel [" + name + "] (" + position + ")";
  }

  @Override
  public Int2D getPosition()
  {
    return position;
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
    position.set(x, y);
  }

  @Override
  protected void finaliseView()
  {
    finalised = true;

    updateBoundingBoxFromShapes(boundingBox);

    selectionBox.copy(this.boundingBox);
    this.boundingBox.grow(0.5f);
  }

  public TunnelData save(boolean selected)
  {
    long[] ids = new long[traces.size()];
    for (int i = 0; i < traces.size(); i++)
    {
      Trace trace = traces.get(i);
      ids[i] = Trace.getId(trace);
    }

    return new TunnelData(ids,
                          getPosition(),
                          selected);
  }

  public Set<TunnelView> getTunnels()
  {
    return tunnels;
  }

  public ConnectionView getConnection()
  {
    return connection;
  }

  public void connectTraces(List<Trace> traces)
  {
    this.traces = traces;
  }

  @Override
  public void disconnectTraces()
  {
    traces = new ArrayList<>();
  }

  @Override
  public List<Trace> getTraces()
  {
    return traces;
  }
}

