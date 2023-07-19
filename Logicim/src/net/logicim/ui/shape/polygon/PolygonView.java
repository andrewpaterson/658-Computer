package net.logicim.ui.shape.polygon;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonView
    extends ShapeView
{
  protected boolean fill;
  protected boolean border;
  protected float width;
  protected List<Tuple2> points;

  protected PolygonViewGridCache gridCache;

  public PolygonView(ShapeHolder shapeHolder,
                     boolean border,
                     boolean fill,
                     Tuple2... points)
  {
    this(shapeHolder,
         null,
         border,
         fill,
         2.0f,
         points);
  }

  public PolygonView(ShapeHolder shapeHolder,
                     Color fillColour,
                     boolean border,
                     boolean fill,
                     float width,
                     Tuple2... points)
  {
    super(shapeHolder, fillColour, null);
    this.fill = fill;
    this.border = border;
    this.width = width;
    this.points = new ArrayList<>(points.length);
    for (Tuple2 point : points)
    {
      this.points.add(point);
    }

    gridCache = new PolygonViewGridCache(this.points);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    List<Tuple2> transformedBuffer = gridCache.getTransformedBuffer();
    int[] xArray = gridCache.getXArray();
    int[] yArray = gridCache.getYArray();
    for (int i = 0; i < transformedBuffer.size(); i++)
    {
      Tuple2 tuple2 = transformedBuffer.get(i);
      xArray[i] = viewport.transformGridToScreenSpaceX(tuple2.getX());
      yArray[i] = viewport.transformGridToScreenSpaceY(tuple2.getY());
    }

    Polygon p = new Polygon(xArray, yArray, transformedBuffer.size());

    graphics.setStroke(viewport.getZoomableStroke(width));
    if (fill)
    {
      graphics.setColor(getFillColour());
      graphics.fillPolygon(p);
    }
    if (border)
    {
      graphics.setColor(getBorderColour());
      graphics.drawPolygon(p);
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    BoundingBox localBox = new BoundingBox();
    for (Tuple2 point : points)
    {
      localBox.include(point);
    }
    transformAndIncludeLocalBox(boundingBox, localBox);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  private void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(points,
                       getShapeHolderRotation(),
                       shapeHolder.getPosition());
    }
  }
}

