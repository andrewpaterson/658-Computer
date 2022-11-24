package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DiscreteView
    extends ComponentView
    implements ShapeHolder
{
  protected CircuitEditor circuitEditor;
  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBox boundingBox;
  protected BoundingBox selectionBox;
  protected List<ShapeView> shapes;
  protected boolean finalised;

  public DiscreteView(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    this.circuitEditor = circuitEditor;
    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = new BoundingBox();
    this.selectionBox = new BoundingBox();
    this.shapes = new ArrayList<>();
    this.finalised = false;
  }

  public void setPosition(int x, int y)
  {
    this.position.set(x, y);

    invalidateCache();
  }

  protected void finaliseView()
  {
    finalised = true;
    updateBoundingBox();
  }

  protected void updateBoundingBox()
  {
    for (ShapeView shape : shapes)
    {
      shape.boundingBoxInclude(boundingBox);
    }

    selectionBox.copy(boundingBox);
    boundingBox.grow(0.5f);
  }

  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if (!finalised)
    {
      throw new SimulatorException("View [" + getClass().getSimpleName() + "] is not finalised.");
    }
  }

  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
    Int2D p = new Int2D();
    Int2D s = new Int2D();

    getSelectionBoxInScreenSpace(viewport, p, s);

    graphics.setStroke(viewport.getStroke());
    Color color = viewport.getColours().getViewHover();
    paintSelectionRectangle(graphics, viewport, p.x, p.y, color);
    paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y, color);
    paintSelectionRectangle(graphics, viewport, p.x, p.y + s.y, color);
    paintSelectionRectangle(graphics, viewport, p.x + s.x, p.y + s.y, color);
  }

  private void paintSelectionRectangle(Graphics2D graphics, Viewport viewport, int x, int y, Color viewHover)
  {
    float zoom = viewport.getZoom();
    float radius = zoom * 3;
    int left = (int) (x - radius);
    int top = (int) (y - radius);
    int width = (int) (radius * 2);
    int height = (int) (radius * 2);
    graphics.setColor(viewHover);
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Color.BLACK);
    graphics.drawRect(left, top, width, height);
  }

  public void paintBoundingBox(Graphics2D graphics, Viewport viewport)
  {
    boundingBox.transform(rotation);

    Int2D p = new Int2D();
    Int2D s = new Int2D();
    getBoundingBoxInScreenSpace(viewport, p, s);
    viewport.paintRectangle(graphics, p.x, p.y, s.x, s.y, viewport.getStroke(1), null, Color.ORANGE);
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

  @Override
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

  protected void invalidateCache()
  {
    for (ShapeView shape : shapes)
    {
      shape.invalidateCache();
    }
  }

  public abstract PortView getPortInGrid(int x, int y);

  public abstract boolean isEnabled();

  public abstract List<PortView> getPorts();

  public abstract void enable(Simulation simulation);

  public abstract void simulationStarted(Simulation simulation);
}

