package net.logicim.ui.common.wire;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.data.wire.TunnelData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.shape.point.PointGridCache;
import net.logicim.ui.shape.polygon.PolygonView;
import net.logicim.ui.shape.text.TextView;

import java.awt.*;
import java.util.List;
import java.util.*;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.LEFT;

public class TunnelView
    extends StaticView<TunnelProperties>
    implements WireView
{
  public static final int FONT_SIZE = 11;

  protected WireViewComp wireView;

  protected Set<TunnelView> tunnels;

  protected TextView textView;
  protected PolygonView polygonView;

  protected Int2D endPosition;
  protected PointGridCache startCache;
  protected PointGridCache endCache;

  protected String sanitisedName;
  protected Int2D startPosition;

  public TunnelView(SubcircuitView containingSubcircuitView,
                    Int2D position,
                    Rotation rotation,
                    TunnelProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    this.wireView = new WireViewComp();
    this.startCache = new PointGridCache(new Int2D());
    this.endCache = new PointGridCache(new Int2D());
    this.sanitisedName = createSanitisedName(properties);
    this.tunnels = containingSubcircuitView.addTunnel(this);
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

  public List<ConnectionView> getOrCreateConnectionViews()
  {
    updateGridCache();

    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    ConnectionView startConnection = containingSubcircuitView.getOrAddConnectionView((Int2D) startCache.getTransformedPosition(), this);
    wireView.setStart(startConnection);
    connectionViews.add(startConnection);
    if (properties.doubleSided)
    {
      ConnectionView endConnection = containingSubcircuitView.getOrAddConnectionView((Int2D) endCache.getTransformedPosition(), this);
      wireView.setEnd(endConnection);
      connectionViews.add(endConnection);
    }
    else
    {
      wireView.setEnd(null);
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
  public void paint(Graphics2D graphics,
                    Viewport viewport, ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    super.paint(graphics, viewport, viewPath, circuitSimulation);

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
  public void simulationStarted(CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public void simulationStarted(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
  }

  @Override
  public void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    wireView.destroyComponent(viewPath, circuitSimulation);
  }

  @Override
  public void destroyAllComponents()
  {
    wireView.destroyAllComponents();
  }

  @Override
  protected void finaliseView()
  {
    super.finaliseView();
    enabled = false;
  }

  @Override
  public void validate()
  {
  }

  public TunnelData save(boolean selected)
  {
    Map<Long, long[]> simulationTraces = wireView.save();

    return new TunnelData(properties.name,
                          position,
                          rotation,
                          id,
                          selected,
                          simulationTraces,
                          enabled,
                          properties.doubleSided);
  }

  @Override
  public void connectTraces(ViewPath viewPath, CircuitSimulation circuitSimulation, List<Trace> traces)
  {
    wireView.connectTraces(viewPath, circuitSimulation, traces);
  }

  @Override
  public void disconnectViewAndDestroyComponents()
  {
    wireView.disconnectViewAndDestroyComponents();
  }

  @Override
  public Traces getTraces(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    return wireView.getTraces(viewPath, circuitSimulation);
  }

  public boolean isRemoved()
  {
    return wireView.getConnections().isEmpty();
  }

  @Override
  public List<ConnectionView> getConnectionViews()
  {
    return wireView.getConnections();
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
        connectionViews.addAll(tunnelView.getConnectionViews());
      }
    }
    return new ArrayList<>(connectionViews);
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Double Sided [%s]\nStart Position [%s]\nEnd Position [%s]\nSanitised Name [%s]\n",
                                                 properties.doubleSided,
                                                 startPosition,
                                                 endPosition,
                                                 sanitisedName);
  }

  public WireViewComp getWireViewComp()
  {
    return wireView;
  }
}

