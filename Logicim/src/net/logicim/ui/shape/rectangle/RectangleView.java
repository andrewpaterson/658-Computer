package net.logicim.ui.shape.rectangle;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;

public class RectangleView
    extends ShapeView
{
  protected Tuple2 dimension;
  protected Tuple2 positionRelativeToIC;
  protected boolean border;
  protected boolean fill;

  protected RectangleGridCache gridCache;

  public RectangleView(ShapeHolder shapeHolder,
                       int width,
                       int height,
                       boolean border,
                       boolean fill)
  {
    super(shapeHolder);
    this.border = border;
    this.fill = fill;
    this.dimension = new Int2D(width, height);
    if ((width % 2 == 0) && (height % 2 == 0))
    {
      positionRelativeToIC = new Int2D(-(width / 2), -(height / 2));
    }
    else
    {
      positionRelativeToIC = new Float2D(-((float) width / 2), -((float) height / 2));
    }

    gridCache = new RectangleGridCache(positionRelativeToIC, dimension);
  }

  public RectangleView(ShapeHolder shapeHolder,
                       Rectangle rectangle,
                       boolean border,
                       boolean fill)
  {
    super(shapeHolder);
    this.border = border;
    this.fill = fill;
    this.dimension = rectangle.getDimension();
    this.positionRelativeToIC = rectangle.getTopLeft().clone();

    this.gridCache = new RectangleGridCache(positionRelativeToIC, dimension);
  }

  public RectangleView(ShapeHolder shapeHolder,
                       Tuple2 topLeft,
                       Tuple2 bottomRight,
                       boolean border,
                       boolean fill)
  {
    super(shapeHolder);
    this.border = border;
    this.fill = fill;
    this.dimension = bottomRight.clone();
    this.dimension.subtract(topLeft);
    this.positionRelativeToIC = topLeft.clone();

    this.gridCache = new RectangleGridCache(positionRelativeToIC, dimension);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    Tuple2 transformedDimension = gridCache.getTransformedDimension();
    Tuple2 transformedPosition = gridCache.getTransformedPosition();

    int x = viewport.transformGridToScreenSpaceX(transformedPosition);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition);
    int width = viewport.transformGridToScreenWidth(transformedDimension);
    int height = viewport.transformGridToScreenHeight(transformedDimension);

    graphics.setStroke(viewport.getZoomableStroke());
    if (fill)
    {
      Color shapeFill = getFillColour();
      graphics.setColor(shapeFill);
      graphics.fillRect(x, y, width, height);
    }
    if (border)
    {
      Color shapeBorder = getBorderColour();
      graphics.setColor(shapeBorder);
      graphics.drawRect(x, y, width, height);
    }
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(positionRelativeToIC,
                       dimension,
                       getShapeHolderRotation(),
                       shapeHolder.getPosition());
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    float x = positionRelativeToIC.getX();
    float y = positionRelativeToIC.getY();
    BoundingBox localBox = new BoundingBox(x, y, x + dimension.getX(), y + dimension.getY());
    transformAndIncludeLocalBox(boundingBox, localBox);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  public Tuple2 getTransformedPosition()
  {
    return gridCache.transformedPosition;
  }

  public Tuple2 getTransformedDimension()
  {
    return gridCache.transformedDimension;
  }
}

