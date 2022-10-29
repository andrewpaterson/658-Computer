package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonView
    extends ShapeView
{
  protected List<Tuple2> points;
  protected List<Tuple2> transformBuffer;
  protected int[] xArray;
  protected int[] yArray;

  public PolygonView(Tuple2... points)
  {
    this.points = new ArrayList<>(points.length);
    this.transformBuffer = new ArrayList<>(points.length);
    for (Tuple2 point : points)
    {
      this.points.add(point);
      this.transformBuffer.add(point.clone());
    }

    xArray = new int[points.length];
    yArray = new int[points.length];
  }

  protected void transform(Rotation rotation, Int2D position, Viewport viewport)
  {
    for (int i = 0; i < points.size(); i++)
    {
      rotate(transformBuffer.get(i), points.get(i), rotation);
    }

    for (int i = 0; i < transformBuffer.size(); i++)
    {
      Tuple2 tuple2 = transformBuffer.get(i);
      if (tuple2 instanceof Int2D)
      {
        xArray[i] = viewport.transformGridToScreenSpaceX(((Int2D) tuple2).x + position.x);
        yArray[i] = viewport.transformGridToScreenSpaceY(((Int2D) tuple2).y + position.y);
      }
      else if (tuple2 instanceof Float2D)
      {
        xArray[i] = viewport.transformGridToScreenSpaceX(((Float2D) tuple2).x + position.x);
        yArray[i] = viewport.transformGridToScreenSpaceY(((Float2D) tuple2).y + position.y);
      }
    }
  }

  public void paint(Graphics2D graphics, Viewport viewport, Rotation rotation, Int2D position)
  {
    transform(rotation, position, viewport);
    Polygon p = new Polygon(xArray, yArray, transformBuffer.size());

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(viewport.getColours().getShapeFill());
    graphics.fillPolygon(p);
    graphics.setColor(viewport.getColours().getShapeBorder());
    graphics.drawPolygon(p);
  }
}

