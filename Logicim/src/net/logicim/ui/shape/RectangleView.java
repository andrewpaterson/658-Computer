package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class RectangleView
    extends ShapeView
{
  protected Tuple2 dimension;
  protected Tuple2 position;

  protected Tuple2 transformedDimension;
  protected Tuple2 transformedPosition;

  public RectangleView(ShapeHolder shapeHolder, int width, int height)
  {
    super(shapeHolder);
    this.dimension = new Int2D(width, height);
    if ((width % 2 == 0) && (height % 2 == 0))
    {
      position = new Int2D(-(width / 2), -(height / 2));
    }
    else
    {
      position = new Float2D(-((float) width / 2), -((float) height / 2));
    }
    transformedPosition = position.clone();
    transformedDimension = dimension.clone();
  }

  public RectangleView(ShapeHolder shapeHolder, int width, int height, int offsetX, int offsetY)
  {
    super(shapeHolder);
    this.dimension = new Int2D(width, height);
    this.position = new Int2D(offsetX, offsetY);
    transformedPosition = position.clone();
    transformedDimension = dimension.clone();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, Rotation rotation, Int2D position)
  {
    int transformedWidth = 0;
    int transformedHeight = 0;
    int transformedLeft = 0;
    int transformedTop = 0;

    rotate(transformedDimension, dimension, rotation);

    if (transformedDimension instanceof Int2D)
    {
      transformedWidth = viewport.transformGridToScreenWidth(((Int2D) transformedDimension).x);
      transformedHeight = viewport.transformGridToScreenHeight(((Int2D) transformedDimension).y);
    }
    else if (transformedDimension instanceof Float2D)
    {
      transformedWidth = viewport.transformGridToScreenWidth(((Float2D) transformedDimension).x);
      transformedHeight = viewport.transformGridToScreenHeight(((Float2D) transformedDimension).y);
    }

    rotate(transformedPosition, this.position, rotation);

    if (transformedDimension instanceof Int2D)
    {
      transformedLeft = viewport.transformGridToScreenSpaceX(position.x + ((Int2D) transformedPosition).x);
      transformedTop = viewport.transformGridToScreenSpaceY(position.y + ((Int2D) transformedPosition).y);
    }
    else if (transformedDimension instanceof Float2D)
    {
      transformedLeft = viewport.transformGridToScreenSpaceX(position.x + ((Float2D) transformedPosition).x);
      transformedTop = viewport.transformGridToScreenSpaceY(position.y + ((Float2D) transformedPosition).y);
    }

    if (transformedWidth < 0)
    {
      transformedLeft += transformedWidth;
      transformedWidth *= -1;
    }
    if (transformedHeight < 0)
    {
      transformedTop += transformedHeight;
      transformedHeight *= -1;
    }

    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(viewport.getColours().getShapeFill());
    graphics.fillRect(transformedLeft, transformedTop, transformedWidth, transformedHeight);
    graphics.setColor(viewport.getColours().getShapeBorder());
    graphics.drawRect(transformedLeft, transformedTop, transformedWidth, transformedHeight);
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    float x = position.getX();
    float y = position.getY();
    boundingBox.include(x, y);
    boundingBox.include(x + dimension.getX(), y + dimension.getY());
  }
}

