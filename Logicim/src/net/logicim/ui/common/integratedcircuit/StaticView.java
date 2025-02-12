package net.logicim.ui.common.integratedcircuit;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.*;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.shape.common.BoundingBox.calculateBoundingBox;

public abstract class StaticView<PROPERTIES extends ComponentProperties>
    extends View
    implements ShapeHolder
{
  protected PROPERTIES properties;

  protected Int2D position;
  protected Rotation rotation;
  protected BoundingBox boundingBox;
  protected BoundingBox selectionBox;
  protected List<ShapeView> shapes;
  protected int relativeRightRotations;

  protected boolean finalised;

  public StaticView(SubcircuitView containingSubcircuitView,
                    Int2D position,
                    Rotation rotation,
                    PROPERTIES properties)
  {
    this(containingSubcircuitView,
         position,
         rotation,
         new BoundingBox(),
         new BoundingBox(),
         properties);
  }

  public StaticView(SubcircuitView containingSubcircuitView,
                    Int2D position,
                    Rotation rotation,
                    BoundingBox boundingBox,
                    BoundingBox selectionBox,
                    PROPERTIES properties)
  {
    super(containingSubcircuitView);
    this.properties = properties;

    this.position = position.clone();
    this.rotation = rotation;
    this.boundingBox = boundingBox;
    this.selectionBox = selectionBox;
    this.shapes = new ArrayList<>();
    this.relativeRightRotations = 0;

    this.finalised = false;
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

  public void setRotation(Rotation rotation)
  {
    this.rotation = rotation;
    invalidateCache();
  }

  protected void updateBoundingBoxFromShapes(BoundingBox boundingBox)
  {
    calculateBoundingBox(boundingBox, shapes);
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
      viewport.paintRectangle(graphics,
                              p.x,
                              p.y,
                              s.x,
                              s.y,
                              viewport.getAbsoluteStroke(1),
                              null,
                              Color.ORANGE);
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

  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    ViewPath viewPath,
                    CircuitSimulation circuitSimulation)
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

  protected void updateBoundingBoxes()
  {
    if (boundingBox.isNull())
    {
      updateBoundingBoxAndSelectionBox();
    }
  }

  protected void updateBoundingBoxAndSelectionBox()
  {
    updateBoundingBoxFromShapes(boundingBox);

    selectionBox.copy(boundingBox);
    boundingBox.grow(0.5f);
  }

  public StaticView<PROPERTIES> duplicate(CircuitEditor circuitEditor,
                                          SubcircuitView subcircuitView,
                                          PROPERTIES properties)
  {
    Class<? extends StaticView<?>> aClass = (Class<? extends StaticView<?>>) getClass();
    ViewFactory viewFactory = ViewFactoryStore.getInstance().get(aClass);
    StaticView<PROPERTIES> newComponentView = viewFactory.create(circuitEditor, subcircuitView, position, rotation, properties);
    newComponentView.setId(id);

    return newComponentView;
  }

  public void recalculatePropertiesAfterNew(SubcircuitView subcircuitView)
  {
  }

  protected void finaliseView()
  {
    setShapeRelativeRights();
    finalised = true;
    updateBoundingBoxes();
  }

  private void setShapeRelativeRights()
  {
    for (ShapeView shape : shapes)
    {
      shape.setRelativeRightRotations(relativeRightRotations);
    }
  }

  public abstract void validate();

  public abstract void clampProperties(PROPERTIES newProperties);

  public abstract ReflectiveData save(boolean selected);

  public abstract String getType();

  public abstract void disconnectViewAndDestroyComponents();

  public abstract void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation);

  public abstract void destroyAllComponents();

  public abstract void simulationStarted(CircuitSimulation circuitSimulation);

  public abstract void simulationStarted(ViewPath viewPath, CircuitSimulation circuitSimulation);

  public abstract List<ConnectionView> getOrCreateConnectionViews();

  public String toDebugString()
  {
    return String.format("Name [%s]\nContaining SubcircuitView [%s]\nPosition [%s]\nRotation [%s]\nFinalised [%s]\n",
                         properties.name,
                         containingSubcircuitView.getTypeName(),
                         position.toString(),
                         rotation.toString(),
                         finalised);
  }
}

