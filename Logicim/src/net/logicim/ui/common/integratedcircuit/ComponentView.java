package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.MultiPortData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ComponentView<PROPERTIES extends ComponentProperties>
    extends View
    implements ShapeHolder
{
  protected PROPERTIES properties;

  protected CircuitEditor circuitEditor;
  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBox boundingBox;
  protected BoundingBox selectionBox;
  protected List<ShapeView> shapes;
  protected boolean finalised;

  protected List<PortView> ports;

  public ComponentView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties)
  {
    this.properties = properties;

    this.circuitEditor = circuitEditor;
    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = new BoundingBox();
    this.selectionBox = new BoundingBox();
    this.shapes = new ArrayList<>();
    this.finalised = false;
    this.ports = new ArrayList<>();
  }

  public void rotateRight()
  {
    rotation = rotation.rotateRight();

    invalidateCache();
  }

  public void rotateLeft()
  {
    rotation = rotation.rotateLeft();

    invalidateCache();
  }

  public void add(ShapeView shapeView)
  {
    shapes.add(shapeView);
  }

  public Int2D getPosition()
  {
    return position;
  }

  public Rotation getRotation()
  {
    return rotation;
  }

  protected void updateBoundingBoxFromShapes(BoundingBox boundingBox)
  {
    for (ShapeView shape : shapes)
    {
      shape.boundingBoxInclude(boundingBox);
    }
  }

  public void paintSelected(Graphics2D graphics, Viewport viewport, Color color)
  {
    Int2D p = new Int2D();
    Int2D s = new Int2D();

    getSelectionBoxInScreenSpace(viewport, p, s);

    graphics.setStroke(viewport.getZoomableStroke());
    paintSelectionRectangle(graphics, viewport, p.x, p.y, color);
    paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y, color);
    paintSelectionRectangle(graphics, viewport, p.x, p.y + s.y, color);
    paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y + s.y, color);
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
    paintSelected(graphics, viewport, Colours.getInstance().getSelected());
  }

  public void paintHover(Graphics2D graphics, Viewport viewport)
  {
    paintSelected(graphics, viewport, Colours.getInstance().getViewHover());
  }

  public void paintBoundingBox(Graphics2D graphics, Viewport viewport)
  {
    boundingBox.transform(rotation);

    Int2D p = new Int2D();
    Int2D s = new Int2D();
    getBoundingBoxInScreenSpace(viewport, p, s);
    viewport.paintRectangle(graphics, p.x, p.y, s.x, s.y, viewport.getAbsoluteStroke(1), null, Color.ORANGE);
  }

  public void getBoundingBoxInScreenSpace(Viewport viewport, Int2D destPosition, Int2D destDimension)
  {
    getBoundingBoxInScreenSpace(viewport, destPosition, destDimension, boundingBox);
  }

  public void getSelectionBoxInScreenSpace(Viewport viewport, Int2D destPosition, Int2D destDimension)
  {
    getBoundingBoxInScreenSpace(viewport, destPosition, destDimension, selectionBox);
  }

  private void getBoundingBoxInScreenSpace(Viewport viewport, Int2D destPosition, Int2D destDimension, BoundingBox boundingBox)
  {
    boundingBox.transform(rotation);

    int x = viewport.transformGridToScreenSpaceX(boundingBox.getTransformedTopLeft().x + position.x);
    int y = viewport.transformGridToScreenSpaceY(boundingBox.getTransformedTopLeft().y + position.y);

    int width = viewport.transformGridToScreenWidth(boundingBox.getTransformedWidth());
    int height = viewport.transformGridToScreenHeight(boundingBox.getTransformedHeight());

    if (width < 0)
    {
      x += width;
      width *= -1;
    }
    if (height < 0)
    {
      y += height;
      height *= -1;
    }

    destPosition.set(x, y);
    destDimension.set(width, height);
  }

  public void getBoundingBoxInGridSpace(Float2D destPosition, Float2D destDimension)
  {
    boundingBox.transform(rotation);

    float x = boundingBox.getTransformedTopLeft().x + position.x;
    float y = boundingBox.getTransformedTopLeft().y + position.y;

    float width = boundingBox.getTransformedWidth();
    float height = boundingBox.getTransformedHeight();

    if (width < 0)
    {
      x += width;
      width *= -1;
    }
    if (height < 0)
    {
      y += height;
      height *= -1;
    }

    destPosition.set(x, y);
    destDimension.set(width, height);
  }

  protected void finaliseView()
  {
    finalised = true;

    updateBoundingBoxFromShapes(boundingBox);
    updateBoundingBoxFromPorts(boundingBox);
    selectionBox.copy(this.boundingBox);
    this.boundingBox.grow(0.5f);
  }

  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if (!finalised)
    {
      throw new SimulatorException("View [" + getClass().getSimpleName() + "] is not finalised.");
    }
  }

  public PortView getPortInGrid(Int2D position)
  {
    return getPortInGrid(position.x, position.y);
  }

  protected void invalidateCache()
  {
    for (ShapeView shape : shapes)
    {
      shape.invalidateCache();
    }

    for (PortView port : ports)
    {
      port.invalidateCache();
    }
  }

  @Override
  public String getName()
  {
    return properties.name;
  }

  public PROPERTIES getProperties()
  {
    return properties;
  }

  public void setProperties(PROPERTIES properties)
  {
    this.properties = properties;
  }

  public abstract String getType();

  protected abstract void createPortViews();

  public void setPosition(int x, int y)
  {
    this.position.set(x, y);
    invalidateCache();
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    PortView portView = getPortInGrid(x, y);
    if (portView != null)
    {
      return portView.getConnection();
    }
    else
    {
      return null;
    }
  }

  public PortView getPortView(Port port)
  {
    for (PortView portView : ports)
    {
      if (portView.containsPort(port))
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics, Viewport viewport, long time)
  {
    for (PortView portView : ports)
    {
      portView.paint(graphics, viewport, time);
    }
  }

  public PortView getPortInGrid(int x, int y)
  {
    for (PortView port : ports)
    {
      if (port.getGridPosition().equals(x, y))
      {
        return port;
      }
    }
    return null;
  }

  public void addPortView(PortView portView)
  {
    ports.add(portView);
  }

  public List<PortView> getPorts()
  {
    return ports;
  }

  protected List<MultiPortData> savePorts()
  {
    List<MultiPortData> portDatas = new ArrayList<>(ports.size());
    for (PortView port : ports)
    {
      MultiPortData portData = port.save();
      portDatas.add(portData);
    }
    return portDatas;
  }

  public PortView getPort(int index)
  {
    return ports.get(index);
  }

  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    for (PortView portView : ports)
    {
      ConnectionView portViewConnections = portView.getConnection();
      if (portViewConnections == connectionView)
      {
        return portView.getGridPosition();
      }
    }
    return null;
  }

  protected void updateBoundingBoxFromPorts(BoundingBox boundingBox)
  {
    for (PortView port : ports)
    {
      port.updateBoundingBox(boundingBox);
    }
  }

  public abstract Component getComponent();

  public abstract ComponentData save(boolean selected);

  @Override
  public void enable(Simulation simulation)
  {
    getComponent().enable(simulation);
  }

  @Override
  public void disable()
  {
    getComponent().disable();
  }

  @Override
  public String getDescription()
  {
    return getComponent().getType() + " " + getName() + " (" + getPosition() + ")";
  }

  public boolean isEnabled()
  {
    return getComponent().isEnabled();
  }

  public abstract void clampProperties();

  public abstract void simulationStarted(Simulation simulation);
}

