package net.logicim.ui.shape.rectangle;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
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

  public RectangleView(ShapeHolder shapeHolder, int width, int height, boolean border, boolean fill)
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

    gridCache = new RectangleGridCache(dimension, positionRelativeToIC);
  }

  public RectangleView(ShapeHolder shapeHolder, Tuple2 position, Tuple2 dimension, boolean border, boolean fill)
  {
    super(shapeHolder);
    this.border = border;
    this.fill = fill;
    this.dimension = dimension.clone();
    this.positionRelativeToIC = position.clone();

    gridCache = new RectangleGridCache(dimension, positionRelativeToIC);
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

    Color shapeFill = viewport.getColours().getShapeFill();
    Color shapeBorder = viewport.getColours().getShapeBorder();

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    if (fill)
    {
      graphics.setColor(shapeFill);
      graphics.fillRect(x, y, width, height);
    }
    if (border)
    {
      graphics.setColor(shapeBorder);
      graphics.drawRect(x, y, width, height);
    }
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(dimension, positionRelativeToIC, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    float x = positionRelativeToIC.getX();
    float y = positionRelativeToIC.getY();
    boundingBox.include(x, y);
    boundingBox.include(x + dimension.getX(), y + dimension.getY());
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }
}

