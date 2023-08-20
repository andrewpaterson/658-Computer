package net.logicim.ui.shape.circle;

import net.common.type.Float2D;
import net.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;

public class CircleView
    extends ShapeView
{
  private Float2D circleCenter;
  private float width;
  private float height;
  private boolean border;
  private boolean fill;

  protected CircleGridCache gridCache;

  public CircleView(ShapeHolder shapeHolder,
                    Float2D circleCenter,
                    float radius,
                    boolean border,
                    boolean fill)
  {
    super(shapeHolder);
    this.shapeHolder = shapeHolder;
    this.circleCenter = circleCenter;
    this.width = radius;
    this.height = radius;
    this.border = border;
    this.fill = fill;

    gridCache = new CircleGridCache(circleCenter, width, height);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    Tuple2 transformedPosition = gridCache.getTransformedCircleCenter();

    int x = viewport.transformGridToScreenSpaceX(transformedPosition);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition);
    int width = viewport.transformGridToScreenWidth(gridCache.getTransformedWidthDiameter());
    int height = viewport.transformGridToScreenHeight(gridCache.getTransformedHeightDiameter());

    graphics.setStroke(viewport.getZoomableStroke());
    if (fill)
    {
      Color shapeFill = getFillColour();
      graphics.setColor(shapeFill);
      graphics.fillOval(x, y, width, height);
    }
    if (border)
    {
      Color shapeBorder = getBorderColour();
      graphics.setColor(shapeBorder);
      graphics.drawOval(x, y, width, height);
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    float x = circleCenter.getX();
    float y = circleCenter.getY();
    BoundingBox localBox = new BoundingBox(x - width, y - height, x + width, y + height);
    transformAndIncludeLocalBox(boundingBox, localBox);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(circleCenter,
                       width,
                       height,
                       getShapeHolderRotation(),
                       shapeHolder.getPosition()
      );
    }
  }
}

