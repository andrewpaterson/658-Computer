package net.logicim.ui.common.wire;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.data.wire.TunnelData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.text.TextView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;

public class TunnelView
    extends StaticView<TunnelProperties>
    implements WireView
{
  public static final int FONT_SIZE = 11;

  protected Set<TunnelView> tunnels;
  protected List<ConnectionView> connections;
  protected List<Trace> traces;
  protected boolean enabled;
  protected TextView textView;
  protected PolygonView polygonView;

  protected Int2D endPosition;
  protected PointGridCache startCache;
  protected PointGridCache endCache;

  protected String sanitisedName;
  protected Int2D startPosition;

  public TunnelView(SubcircuitView subcircuitView,
                    Int2D position,
                    Rotation rotation,
                    TunnelProperties properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.enabled = false;
    this.connections = new ArrayList<>(2);
    this.connections.add(null);
    this.connections.add(null);
    this.startCache = new PointGridCache(new Int2D());
    this.endCache = new PointGridCache(new Int2D());
    this.traces = new ArrayList<>();
    this.sanitisedName = createSanitisedName(properties);
    this.tunnels = subcircuitView.addTunnel(this);
    this.startPosition = new Int2D(0, 0);
    this.endPosition = createGraphics();

    //You probably broke bounding boxes on this.
    finaliseView();
  }

  protected String createSanitisedName(TunnelProperties properties)
  {
    return properties.name.trim().toLowerCase();
  }

  private Int2D createGraphics()
  {
    float offsetX = 0.75f;
    float flatX = 0.4f;
    textView = new TextView(this,
                            new Float2D(startPosition.x, startPosition.y + offsetX - 1.25f),
                            properties.name,
                            SANS_SERIF,
                            FONT_SIZE,
                            true,
                            LEFT);
    Float2D topLeft = textView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(textView.getTextDimension());
    bottomRight.add(topLeft);
    float height = bottomRight.y - topLeft.y - 0.25f;
    offsetX = height / 2;

    Int2D endPosition;
    if (!properties.doubleSided)
    {
      endPosition = new Int2D(bottomRight.x + offsetX + offsetX, 0);
      polygonView = new PolygonView(this,
                                    true,
                                    true,
                                    toFloat2D(startPosition.x, startPosition.y),
                                    toFloat2D(topLeft.x + offsetX, topLeft.y),
                                    toFloat2D(bottomRight.x + offsetX + flatX, topLeft.y),
                                    toFloat2D(bottomRight.x + offsetX + flatX, bottomRight.y),
                                    toFloat2D(topLeft.x + offsetX, bottomRight.y));
    }
    else
    {
      endPosition = new Int2D(bottomRight.x + offsetX + offsetX + 0.5f, 0);
      polygonView = new PolygonView(this,
                                    true,
                                    true,
                                    toFloat2D(startPosition.x, startPosition.y),
                                    toFloat2D(topLeft.x + offsetX, topLeft.y),
                                    toFloat2D(endPosition.x - offsetX, topLeft.y),
                                    toFloat2D(endPosition.x, endPosition.y),
                                    toFloat2D(endPosition.x - offsetX, bottomRight.y),
                                    toFloat2D(topLeft.x + offsetX, bottomRight.y));
    }
    invalidateCache();
    updateBoundingBoxes();

    Rotation.East.rotate(endPosition, endPosition);
    return endPosition;
  }

  private Float2D toFloat2D(float x, float y)
  {
    Float2D f = new Float2D(x, y);
    Rotation.East.rotate(f, f);
    return f;
  }

  public List<ConnectionView> createConnectionViews(SubcircuitView subcircuitView)
  {
    updateGridCache();

    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    ConnectionView startConnection = subcircuitView.getOrAddConnectionView((Int2D) startCache.getTransformedPosition(), this);
    this.connections.set(0, startConnection);
    connectionViews.add(startConnection);
    if (properties.doubleSided)
    {
      ConnectionView endConnection = subcircuitView.getOrAddConnectionView((Int2D) endCache.getTransformedPosition(), this);
      this.connections.set(1, endConnection);
      connectionViews.add(endConnection);
    }
    else
    {
      this.connections.set(1, null);
    }
    return connectionViews;
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
  public void paint(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation)
  {
    super.paint(graphics, viewport, simulation);

    Color color = graphics.getColor();
    Stroke stroke = graphics.getStroke();
    Font font = graphics.getFont();

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
  public void enable(CircuitSimulation simulation)
  {
    enabled = true;
  }

  @Override
  public void disable(CircuitSimulation simulation)
  {
    enabled = false;
  }

  @Override
  public boolean isEnabled(CircuitSimulation simulation)
  {
    return enabled;
  }

  @Override
  public void clampProperties(TunnelProperties newProperties)
  {
    String sanitisedName = createSanitisedName(newProperties);
    if (sanitisedName.isEmpty())
    {
      newProperties.name = "  ";
    }
  }

  @Override
  public String getType()
  {
    return "Tunnel";
  }

  @Override
  public void simulationStarted(CircuitSimulation simulation)
  {
  }

  @Override
  protected void finaliseView()
  {
    finalised = true;
    enabled = false;

    updateBoundingBoxes();
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
                          id,
                          selected,
                          ids,
                          properties.doubleSided);
  }

  public void connectTraces(List<Trace> traces)
  {
    this.traces = traces;
  }

  @Override
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

  @Override
  public List<Trace> getTraces()
  {
    return traces;
  }

  public boolean isRemoved()
  {
    return connections.isEmpty();
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

  public String getSanitisedName()
  {
    return sanitisedName;
  }

  public List<ConnectionView> getAllConnectedTunnelConnections()
  {
    Set<ConnectionView> connectionViews = new HashSet<>();
    if (tunnels != null)
    {
      for (TunnelView tunnelView : tunnels)
      {
        connectionViews.addAll(tunnelView.getConnections());
      }
    }
    return new ArrayList<>(connectionViews);
  }
}

