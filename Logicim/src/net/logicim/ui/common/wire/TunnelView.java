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
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
  private Int2D startPosition;

  public TunnelView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    TunnelProperties properties)
  {
    this(circuitEditor, position, rotation, new BoundingBox(), new BoundingBox(), properties);
  }

  public TunnelView(CircuitEditor circuitEditor,
                    Int2D position,
                    Rotation rotation,
                    BoundingBox boundingBox,
                    BoundingBox selectionBox,
                    TunnelProperties properties)
  {
    super(circuitEditor, position, rotation, boundingBox, selectionBox, properties);
    this.enabled = false;
    this.connections = new ArrayList<>(2);
    this.connections.add(null);
    this.connections.add(null);
    this.startCache = new PointGridCache(new Int2D());
    this.endCache = new PointGridCache(new Int2D());
    this.traces = new ArrayList<>();
    this.sanitisedName = createSanitisedName(properties);
    this.tunnels = circuitEditor.addTunnel(this);
    this.startPosition = new Int2D(0, 0);
    this.endPosition = createGraphics();

    createConnections(circuitEditor);
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
                            new Float2D(startPosition.x + offsetX, startPosition.y),
                            properties.name,
                            FONT_SIZE,
                            true,
                            LEFT);
    Float2D topLeft = textView.getTextOffset().clone();
    Float2D bottomRight = new Float2D(textView.getTextDimension());
    bottomRight.add(topLeft);
    float height = bottomRight.y - topLeft.y;
    offsetX = height / 2;

    Int2D endPosition = new Int2D(bottomRight.x + offsetX + offsetX, 0);

    if (!properties.doubleSided)
    {
      polygonView = new PolygonView(this,
                                    true,
                                    true,
                                    startPosition,
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
                                    startPosition,
                                    new Float2D(topLeft.x + offsetX, topLeft.y),
                                    new Float2D(endPosition.x - offsetX, topLeft.y),
                                    endPosition,
                                    new Float2D(endPosition.x - offsetX, bottomRight.y),
                                    new Float2D(topLeft.x + offsetX, bottomRight.y));
    }
    invalidateCache();
    updateBoundingBox();

    return endPosition;
  }

  public void createConnections(CircuitEditor circuitEditor)
  {
    updateGridCache();

    this.connections.set(0, circuitEditor.getOrAddConnection((Int2D) startCache.getTransformedPosition(), this));
    if (properties.doubleSided)
    {
      this.connections.set(1, circuitEditor.getOrAddConnection((Int2D) endCache.getTransformedPosition(), this));
    }
    else
    {
      this.connections.set(1, null);
    }
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
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

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
  public void propertyChanged()
  {
    sanitisedName = createSanitisedName(properties);
    if (sanitisedName.isEmpty())
    {
      properties.name = "  ";
    }
    endPosition = null;
  }

  @Override
  public String getType()
  {
    return "Tunnel";
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
                          boundingBox.save(),
                          selectionBox.save(),
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
  public void disconnect(Simulation simulation, ConnectionView connection)
  {
    int index = connections.indexOf(connection);
    if (index == -1)
    {
      throw new SimulatorException("Could not disconnect connection from %s.", toIdentifierString());
    }
    connections.set(index, null);
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

  public Set<ConnectionView> getAllConnectedTunnelConnections()
  {
    LinkedHashSet<ConnectionView> connectionViews = new LinkedHashSet<>();
    if (tunnels != null)
    {
      for (TunnelView tunnelView : tunnels)
      {
        connectionViews.addAll(tunnelView.getConnections());
      }
    }
    return connectionViews;
  }
}

