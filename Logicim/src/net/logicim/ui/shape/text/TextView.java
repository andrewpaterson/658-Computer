package net.logicim.ui.shape.text;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.point.PointGridCache;

import javax.swing.*;
import java.awt.*;

public class TextView
    extends ShapeView
{
  protected Tuple2 positionRelativeToIC;
  protected String text;
  protected float size;
  protected boolean bold;

  protected int horizontalAlignment;
  protected int verticalAlignment;

  protected PointGridCache gridCache;

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  int horizontalAlignment,
                  int verticalAlignment)
  {
    super(shapeHolder);
    this.positionRelativeToIC = positionRelativeToIC;
    this.text = text;
    this.size = size;
    this.bold = bold;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;

    this.gridCache = new PointGridCache(positionRelativeToIC);
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(positionRelativeToIC, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    Tuple2 transformed = gridCache.getTransformed();
    int x = viewport.transformGridToScreenSpaceX(transformed);
    int y = viewport.transformGridToScreenSpaceY(transformed);

    Font font = viewport.getFont(size * viewport.getZoom(), bold);
    FontMetrics metrics = graphics.getFontMetrics(font);
    int xOffset = 0;
    if (horizontalAlignment == SwingConstants.CENTER)
    {
      xOffset = (-metrics.stringWidth(text)) / 2;
    }
    else if (horizontalAlignment == SwingConstants.RIGHT)
    {
      xOffset = -metrics.stringWidth(text);
    }
    int yOffset = 0;
    if (verticalAlignment == SwingConstants.BOTTOM)
    {
      yOffset = -metrics.getHeight() + metrics.getAscent();
    }
    else if (verticalAlignment == SwingConstants.CENTER)
    {
      yOffset = ((-metrics.getHeight()) / 2) + metrics.getAscent();
    }
    else if (verticalAlignment == SwingConstants.TOP)
    {
      yOffset = metrics.getAscent();
    }

    int sx = x + xOffset;
    int sy = y + yOffset;
    graphics.setFont(font);
    graphics.drawString(text, sx, sy);

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

