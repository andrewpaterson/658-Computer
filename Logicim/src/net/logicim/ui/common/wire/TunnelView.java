package net.logicim.ui.common.wire;

import net.logicim.common.SimulatorException;
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
import net.logicim.ui.shape.point.PointGridCache;
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
  protected List<ConnectionView> connections;
  protected List<Trace> traces;
  protected boolean enabled;
  protected TextView textView;
  protected PolygonView polygonView;

  protected Int2D startPosition;
  protected Int2D endPosition;
  protected PointGridCache startCache;
  protected PointGridCache endCache;

  public TunnelView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, TunnelProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    this.enabled = false;
    this.connections = new ArrayList<>(2);
    this.startPosition = new Int2D(0, 0);
    this.startCache = new PointGridCache(startPosition);
    this.endCache = new PointGridCache(startPosition);
    this.connections.add(circuitEditor.getOrAddConnection(startPosition, this));
    this.traces = new ArrayList<>();
    this.tunnels = circuitEditor.addTunnel(this);
    finaliseView();
  }

  private void createGraphics(Graphics2D graphics, Viewport viewport)
  {
    float offsetX = 0.75f;
    float flatX = 0.4f;
    textView = new TextView(this,
                            new Float2D(offsetX, 0),
                            properties.name,
                            11,
                            true,
                            LEFT);
    textView.updateDimension(graphics, viewport);

    Float2D topLeft = textView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(textView.getTextDimension());
    bottomRight.add(topLeft);
    float middleY = (topLeft.y + bottomRight.y) / 2;
    float height = bottomRight.y - topLeft.y;
    offsetX = height / 2;

    if (!properties.doubleSided)
    {
      polygonView = new PolygonView(this,
                                    true,
                                    true,
                                    new Float2D(0, middleY),
                                    new Float2D(topLeft.x + offsetX, topLeft.y),
                                    new Float2D(bottomRight.x + offsetX + flatX, topLeft.y),
                                    new Float2D(bottomRight.x + offsetX + flatX, bottomRight.y),
                                    new Float2D(topLeft.x + offsetX, bottomRight.y));
    }
    else
    {
      polygonView = new PolygonView(this,
                                    true,
                                    true,
                                    new Float2D(0, middleY),
                                    new Float2D(topLeft.x + offsetX, topLeft.y),
                                    new Float2D(bottomRight.x + offsetX, topLeft.y),
                                    new Float2D(bottomRight.x + offsetX + offsetX, middleY),
                                    new Float2D(bottomRight.x + offsetX, bottomRight.y),
                                    new Float2D(topLeft.x + offsetX, bottomRight.y));

    }

    updateBoundingBox();
  }

  public ConnectionView getStartConnection()
  {
    if (connections.size() > 0)
    {
      return connections.get(0);
    }
    return null;
  }

  public ConnectionView getEndConnection()
  {
    if (connections.size() > 1)
    {
      return connections.get(1);
    }
    return null;
  }

  public void updateGridCache()
  {
    if (!startCache.isValid())
    {
      startCache.update(startPosition, rotation, position);
      if (properties.doubleSided)
      {
        endCache.update(endPosition, rotation, position);
      }
    }
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    updateGridCache();

    if (startCache.getTransformedPosition().equals(x, y))
    {
      return getStartConnection();
    }
    if (properties.doubleSided && endCache.getTransformedPosition().equals(x, y))
    {
      return getEndConnection();
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    updateGridCache();

    if (getStartConnection() == connectionView)
    {
      return (Int2D) startCache.getTransformedPosition();
    }
    if (properties.doubleSided && getEndConnection() == connectionView)
    {
      return (Int2D) endCache.getTransformedPosition();
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

    return new TunnelData(properties.name,
                          position,
                          rotation,
                          selected,
                          ids,
                          properties.doubleSided);
  }

  public Set<TunnelView> getTunnels()
  {
    return tunnels;
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
    return connections.isEmpty();
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    return connections;
  }

  @Override
  public void removed()
  {
    connections.clear();

    if (!traces.isEmpty())
    {
      throw new SimulatorException("Trace must be disconnected before removal.");
    }
  }

  @Override
  public TunnelView getView()
  {
    return this;
  }

  @Override
  protected void invalidateCache()
  {
    super.invalidateCache();
    startCache.invalidate();
    endCache.invalidate();
  }
}

