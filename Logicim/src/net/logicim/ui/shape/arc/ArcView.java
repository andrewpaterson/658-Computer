package net.logicim.ui.shape.arc;

import net.logicim.common.type.Float2D;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;

public class ArcView
    extends ShapeView
{
  private Float2D circleCenter;
  private float width;
  private int startAngle;
  private int arcAngle;
  private boolean border;
  private boolean fill;

  private ArcViewGridCache gridCache;

  public ArcView(ShapeHolder shapeHolder,
                 Float2D circleCenter,
                 float width,
                 int startAngle,
                 int arcAngle,
                 boolean border,
                 boolean fill)
  {
    super(shapeHolder);
    this.circleCenter = circleCenter;
    this.width = width;
    this.startAngle = startAngle;
    this.arcAngle = arcAngle;
    this.border = border;
    this.fill = fill;

    this.gridCache = new ArcViewGridCache();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    int x = viewport.transformGridToScreenSpaceX(gridCache.circleCenter.x);
    int y = viewport.transformGridToScreenSpaceY(gridCache.circleCenter.y);

    graphics.setStroke(viewport.getZoomableStroke());
    if (fill)
    {
      graphics.setColor(getFillColour());
      int diameter = viewport.transformGridToScreenWidth(gridCache.diameter);
      graphics.fillArc(x, y, diameter, diameter, gridCache.startAngle, arcAngle);
    }
    if (border)
    {
      graphics.setColor(getBorderColour());
      int diameter = viewport.transformGridToScreenWidth(gridCache.diameter);
      graphics.drawArc(x, y, diameter, diameter, gridCache.startAngle, arcAngle);
    }
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(shapeHolder.getRotation(), shapeHolder.getPosition(), circleCenter, width, startAngle);
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {

  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }
}

