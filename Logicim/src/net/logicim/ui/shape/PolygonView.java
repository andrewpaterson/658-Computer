package net.logicim.ui.shape;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonView
    extends ShapeView
{
  protected List<Tuple2> points;

  protected PolygonViewGridCache gridCache;

  public PolygonView(ShapeHolder shapeHolder, Tuple2... points)
  {
    super(shapeHolder);
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

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(viewport.getColours().getShapeFill());
    graphics.fillPolygon(p);
    graphics.setColor(viewport.getColours().getShapeBorder());
    graphics.drawPolygon(p);
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    for (Tuple2 point : points)
    {
      boundingBox.include(point);
    }
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
      gridCache.update(points, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }
}

