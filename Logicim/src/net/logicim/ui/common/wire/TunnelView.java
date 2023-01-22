package net.logicim.ui.common.wire;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.wire.TunnelData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;

public class TunnelView
    extends StaticView<TunnelProperties>
    implements WireView
{
  protected Set<TunnelView> tunnels;
  protected ConnectionView startConnection;
  protected ConnectionView EndConnection;
  protected List<Trace> traces;
  protected Int2D position;
  protected boolean enabled;
  protected TextView textView;
  protected PolygonView polygonView;

  public TunnelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, TunnelProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    this.enabled = false;
    this.startConnection = circuitEditor.getOrAddConnection(position, this);
    this.position = position.clone();
    this.traces = new ArrayList<>();
    this.tunnels = circuitEditor.addTunnel(this);
    finaliseView();
  }

  private void createGraphics(Graphics2D graphics, Viewport viewport)
  {
    textView = new TextView(this,
                            new Int2D(1, 0),
                            properties.name,
                            11,
                            true,
                            LEFT);
    textView.updateDimension(graphics, viewport);

    Float2D topLeft = textView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(textView.getTextDimension());
    bottomRight.add(topLeft);

    polygonView = new PolygonView(this,
                                  true,
                                  true,
                                  topLeft,
                                  new Float2D(bottomRight.x, topLeft.y),
                                  bottomRight,
                                  new Float2D(topLeft.x, bottomRight.y));

    updateBoundingBox();

  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (position.equals(x, y))
    {
      return startConnection;
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    if (this.startConnection == connectionView)
    {
      return position;
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

    if (textView == null)
    {
      createGraphics(graphics, viewport);
    }

    if (polygonView != null)
    {
      polygonView.paint(graphics, viewport);
    }

    if (textView != null)
    {
      graphics.setColor(Color.BLACK);
      textView.paint(graphics, viewport);
    }

    graphics.setFont(font);
    graphics.setColor(color);
    graphics.setStroke(stroke);

  }

  @Override
  public String getName()
  {
    return properties.name;
  }

  @Override
  public String getDescription()
  {
    return "Tunnel [" + getName() + "] (" + position + ")";
  }

  @Override
  public Int2D getPosition()
  {
    return position;
  }

  @Override
  public void enable(Simulation simulation)
  {
    enabled = true;
  }

  @Override
  public void disable()
  {
    enabled = false;
  }

  @Override
  public boolean isEnabled()
  {
    return enabled;
  }

  @Override
  public PortView getPortInGrid(int x, int y)
  {
    return null;
  }

  @Override
  public void propertyChanged()
  {
  }

  @Override
  public String getType()
  {
    return "Tunnel";
  }

  @Override
  public List<PortView> getPorts()
  {
    return Collections.emptyList();
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public Component getComponent()
  {
    return null;
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
    enabled = true;

    updateBoundingBox();
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

  public ConnectionView getStartConnection()
  {
    return startConnection;
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

  @Override
  public boolean isRemoved()
  {
    return startConnection == null || endConnection == null;return false;
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    return null;
  }

  @Override
  public void removed()
  {

  }

  @Override
  public TunnelView getView()
  {
    return this;
  }
}

