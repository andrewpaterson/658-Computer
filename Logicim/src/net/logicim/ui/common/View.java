package net.logicim.ui.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.shape.BoundingBox;
import net.logicim.ui.shape.ShapeView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class View
    implements ShapeHolder
{
  protected CircuitEditor circuitEditor;
  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBox boundingBox;
  protected List<ShapeView> shapes;
  protected boolean finalised;

  public View(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    this.circuitEditor = circuitEditor;
    this.position = position;
    this.rotation = rotation;
    circuitEditor.add(this);
    this.boundingBox = new BoundingBox();
    this.shapes = new ArrayList<>();
    this.finalised = false;
  }

  public void setPosition(int x, int y)
  {
    this.position.set(x, y);
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

    boundingBox.grow(0.5f);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if (!finalised)
    {
      throw new SimulatorException("View [" + getClass().getSimpleName() + "] is not finalised.");
    }
  }

  protected void paintBoundingBox(Graphics2D graphics, Viewport viewport)
  {
    boundingBox.transform(rotation);
    int x = viewport.transformGridToScreenSpaceX(boundingBox.getTransformedTopLeft().x + position.x);
    int y = viewport.transformGridToScreenSpaceY(boundingBox.getTransformedTopLeft().y + position.y);
    int width = viewport.transformGridToScreenWidth(boundingBox.getTransformedWidth());
    int height = viewport.transformGridToScreenHeight(boundingBox.getTransformedHeight());

    viewport.paintRectangle(graphics, x, y, width, height, new BasicStroke(1), null, Color.ORANGE);
  }

  public void rotateRight()
  {
    rotation = rotation.rotateRight();
  }

  public void rotateLeft()
  {
    rotation = rotation.rotateLeft();
  }

  public void enable(Simulation simulation)
  {
  }

  @Override
  public void add(ShapeView shapeView)
  {
    shapes.add(shapeView);
  }
}

