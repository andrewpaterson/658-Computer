package net.logicim.ui.shape.line;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;

public class LineView
    extends ShapeView
{
  protected Tuple2 start;
  protected Tuple2 end;

  protected LineGridCache gridCache;

  public LineView(ShapeHolder shapeHolder, Tuple2 start, Tuple2 end)
  {
    super(shapeHolder);
    this.start = start.clone();
    this.end = end.clone();

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

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(viewport.getColours().getShapeBorder());

    graphics.drawLine(x1, y1, x2, y2);
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(start, end, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    boundingBox.include(start);
    boundingBox.include(end);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }
}

