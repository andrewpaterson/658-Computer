package net.logicim.ui.shape.line;

import net.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;

public class LineView
    extends ShapeView
{
  protected float width;
  protected Tuple2 start;
  protected Tuple2 end;

  protected LineGridCache gridCache;

  public LineView(ShapeHolder shapeHolder, Tuple2 start, Tuple2 end)
  {
    this(shapeHolder, start, end, 2);
  }

  public LineView(ShapeHolder shapeHolder, Tuple2 start, Tuple2 end, float width)
  {
    super(shapeHolder);
    this.start = start.clone();
    this.end = end.clone();
    this.width = width;

    this.gridCache = new LineGridCache(start, end);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    Tuple2 transformedStart = gridCache.getTransformedStart();
    Tuple2 transformedEnd = gridCache.getTransformedEnd();

    int x1 = viewport.transformGridToScreenSpaceX(transformedStart);
    int y1 = viewport.transformGridToScreenSpaceY(transformedStart);
    int x2 = viewport.transformGridToScreenSpaceX(transformedEnd);
    int y2 = viewport.transformGridToScreenSpaceY(transformedEnd);

    graphics.setStroke(viewport.getZoomableStroke(width));
    graphics.setColor(getBorderColour());

    graphics.drawLine(x1, y1, x2, y2);
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(start,
                       end,
                       getShapeHolderRotation(),
                       shapeHolder.getPosition());
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    BoundingBox localBox = new BoundingBox(start.getX(), start.getY(), end.getX(), end.getY());
    transformAndIncludeLocalBox(boundingBox, localBox);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }
}

