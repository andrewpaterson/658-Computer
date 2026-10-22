package net.logicim.ui.shape.arc;

import net.common.type.Float2D;
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
  private float height;
  private int startAngle;
  private int arcAngle;
  private boolean border;
  private boolean fill;

  private ArcViewGridCache gridCache;

  public ArcView(ShapeHolder shapeHolder,
                 Float2D circleCenter,
                 float radius,
                 int startAngle,
                 int arcAngle,
                 boolean border,
                 boolean fill)
  {
    this(shapeHolder,
         circleCenter,
         radius,
         radius,
         startAngle,
         arcAngle,
         border,
         fill);
  }

  public ArcView(ShapeHolder shapeHolder,
                 Float2D circleCenter,
                 float width,
                 float height,
                 int startAngle,
                 int arcAngle,
                 boolean border,
                 boolean fill)
  {
    super(shapeHolder);
    this.circleCenter = circleCenter;
    this.width = width;
    this.height = height;
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
    int widthDiameter = viewport.transformGridToScreenWidth(gridCache.widthDiameter);
    int heightDiameter = viewport.transformGridToScreenHeight(gridCache.heightDiameter);
    if (fill)
    {
      graphics.setColor(getFillColour());
      graphics.fillArc(x, y, widthDiameter, heightDiameter, gridCache.startAngle, arcAngle);
    }
    if (border)
    {
      graphics.setColor(getBorderColour());
      graphics.drawArc(x, y, widthDiameter, heightDiameter, gridCache.startAngle, arcAngle);
    }
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(getShapeHolderRotation(),
                       shapeHolder.getPosition(),
                       circleCenter,
                       width,
                       height,
                       startAngle);
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

