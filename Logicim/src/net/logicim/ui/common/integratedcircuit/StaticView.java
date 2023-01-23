package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class StaticView<PROPERTIES extends ComponentProperties>
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

  public StaticView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties)
  {
    this(circuitEditor, position, rotation, new BoundingBox(), new BoundingBox(), properties);
  }

  public StaticView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, BoundingBox boundingBox, BoundingBox selectionBox, PROPERTIES properties)
  {
    this.properties = properties;

    this.circuitEditor = circuitEditor;
    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = boundingBox;
    this.selectionBox = selectionBox;
    this.shapes = new ArrayList<>();

    this.finalised = false;
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

    if (getSelectionBoxInScreenSpace(viewport, p, s))
    {
      graphics.setStroke(viewport.getZoomableStroke());
      paintSelectionRectangle(graphics, viewport, p.x, p.y, color);
      paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y, color);
      paintSelectionRectangle(graphics, viewport, p.x, p.y + s.y, color);
      paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y + s.y, color);
    }
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
    if (getBoundingBoxInScreenSpace(viewport, p, s))
    {
      viewport.paintRectangle(graphics, p.x, p.y, s.x, s.y, viewport.getAbsoluteStroke(1), null, Color.ORANGE);
    }
  }

  public boolean getBoundingBoxInScreenSpace(Viewport viewport, Int2D destPosition, Int2D destDimension)
  {
    if (boundingBox != null && !boundingBox.isNull())
    {
      getBoundingBoxInScreenSpace(viewport, destPosition, destDimension, boundingBox);
      return true;
    }
    return false;
  }

  public boolean getSelectionBoxInScreenSpace(Viewport viewport, Int2D destPosition, Int2D destDimension)
  {
    if (selectionBox != null && !selectionBox.isNull())
    {
      getBoundingBoxInScreenSpace(viewport, destPosition, destDimension, selectionBox);
      return true;
    }
    return false;
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

  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if (!finalised)
    {
      throw new SimulatorException("View [" + getClass().getSimpleName() + "] is not finalised.");
    }
  }

  protected void invalidateCache()
  {
    for (ShapeView shape : shapes)
    {
      shape.invalidateCache();
    }
  }

  public void setPosition(int x, int y)
  {
    this.position.set(x, y);
    invalidateCache();
  }

  public PROPERTIES getProperties()
  {
    return properties;
  }

  protected void updateBoundingBox()
  {
    if (boundingBox.isNull())
    {
      updateSelectionBox();
    }
  }

  protected void updateSelectionBox()
  {
    updateBoundingBoxFromShapes(boundingBox);

    selectionBox.copy(boundingBox);
    boundingBox.grow(0.5f);
  }

  public void setRotation(Rotation rotation)
  {
    this.rotation = rotation;
    invalidateCache();
  }

  public void setBoundingBox(Float2D topLeft, Float2D bottomRight)
  {
    boundingBox.setTopLeft(topLeft);
    boundingBox.setBottomRight(bottomRight);
  }

  protected abstract void finaliseView();

  public abstract boolean isEnabled();

  public abstract PortView getPortInGrid(int x, int y);

  public abstract void propertyChanged();

  public abstract ReflectiveData save(boolean selected);

  public abstract String getType();

  public abstract List<PortView> getPorts();

  public abstract void simulationStarted(Simulation simulation);

  public abstract Component getComponent();
}

