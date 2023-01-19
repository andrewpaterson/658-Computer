package net.logicim.ui.shape.text;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.point.PointGridCache;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class TextView
    extends ShapeView
{
  protected Tuple2 positionRelativeToIC;
  protected String text;
  protected float size;
  protected boolean bold;
  protected boolean useHolderRotation;
  protected Rotation rotation;

  protected int horizontalAlignment;
  protected int verticalAlignment;

  protected PointGridCache gridCache;

  public TextView(ShapeHolder shapeHolder, Tuple2 positionRelativeToIC, String text, float size, boolean bold)
  {
    this(shapeHolder, positionRelativeToIC, text, size, bold, SwingConstants.LEFT, SwingConstants.TOP);
  }

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  int horizontalAlignment,
                  int verticalAlignment)
  {
    this(shapeHolder, positionRelativeToIC, text, size, bold, false, Rotation.North, horizontalAlignment, verticalAlignment);
  }

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  boolean useHolderRotation,
                  Rotation rotation,
                  int horizontalAlignment,
                  int verticalAlignment)
  {
    super(shapeHolder);
    this.positionRelativeToIC = positionRelativeToIC;
    this.text = text;
    this.size = size;
    this.bold = bold;
    this.useHolderRotation = useHolderRotation;
    this.rotation = rotation;
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
    Rotation rotation = getRotation();

    int xOffset;
    if (rotation.isNorthSouth() || rotation == Rotation.Cannot)
    {
      xOffset = 0;
      if (horizontalAlignment == SwingConstants.CENTER)
      {
        xOffset = (-metrics.stringWidth(text)) / 2;
      }
      else if (horizontalAlignment == SwingConstants.RIGHT)
      {
        xOffset = -metrics.stringWidth(text);
      }
    }
    else
    {
      xOffset = 0;
      if (verticalAlignment == SwingConstants.BOTTOM)
      {
        xOffset = -metrics.getHeight() + metrics.getAscent();
      }
      else if (verticalAlignment == SwingConstants.CENTER)
      {
        xOffset = ((-metrics.getHeight()) / 2) + metrics.getAscent();
      }
      else if (verticalAlignment == SwingConstants.TOP)
      {
        xOffset = metrics.getAscent();
      }
    }

    int yOffset;
    if (rotation.isNorthSouth() || rotation == Rotation.Cannot)
    {
      yOffset = 0;
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
    }
    else
    {
      yOffset = 0;
      if (horizontalAlignment == SwingConstants.CENTER)
      {
        yOffset = (-metrics.stringWidth(text)) / 2;
      }
      else if (horizontalAlignment == SwingConstants.RIGHT)
      {
        yOffset = -metrics.stringWidth(text);
      }
    }

    int sx = x + xOffset;
    int sy = y + yOffset;

    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(Math.toRadians(getRotation().rotationToDegrees()), 0, 0);
    font = font.deriveFont(affineTransform);

    graphics.setFont(font);
    graphics.drawString(text, sx, sy);
  }

  protected Rotation getRotation()
  {
    if (useHolderRotation)
    {
      return shapeHolder.getRotation();
    }
    else
    {
      return rotation;
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

  public void setParameters(int horizontalAlignment,
                            int verticalAlignment)
  {
    setParameters(false, Rotation.North, horizontalAlignment, verticalAlignment);
  }

  public void setParameters(boolean useHolderRotation,
                            Rotation rotation,
                            int horizontalAlignment,
                            int verticalAlignment)
  {
    this.useHolderRotation = useHolderRotation;
    this.rotation = rotation;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;

    invalidateCache();
  }

  public void setPositionRelativeToIC(Tuple2 positionRelativeToIC)
  {
    this.positionRelativeToIC = positionRelativeToIC;

    invalidateCache();
  }
}

