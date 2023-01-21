package net.logicim.ui.shape.text;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.point.PointGridCache;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static net.logicim.ui.shape.text.HorizontalAlignment.CENTER;
import static net.logicim.ui.shape.text.HorizontalAlignment.RIGHT;

public class TextView
    extends ShapeView
{
  protected Tuple2 positionRelativeToIC;
  protected Float2D textOffset;
  protected Float2D textDimension;

  protected String text;
  protected float size;
  protected boolean bold;
  protected boolean useHolderRotation;
  protected Rotation rotation;
  protected HorizontalAlignment alignment;

  protected PointGridCache gridCache;

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  HorizontalAlignment alignment)
  {
    this(shapeHolder, positionRelativeToIC, text, size, bold, false, Rotation.North, alignment);
  }

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  boolean useHolderRotation,
                  Rotation rotation,
                  HorizontalAlignment alignment)
  {
    super(shapeHolder);
    this.positionRelativeToIC = positionRelativeToIC;
    this.alignment = alignment;
    this.textDimension = null;

    this.text = text;
    this.size = size;
    this.bold = bold;
    this.useHolderRotation = useHolderRotation;
    this.rotation = rotation;

    this.gridCache = new PointGridCache(positionRelativeToIC);
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(positionRelativeToIC, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }

  public void updateDimension(Graphics2D graphics, Viewport viewport)
  {
    if (textDimension == null)
    {
      Font font = viewport.getFont(size, bold);
      FontMetrics metrics = graphics.getFontMetrics(font);

      int height = metrics.getAscent() + metrics.getDescent();
      int width = metrics.stringWidth(text);
      float halfHeight = (float) height / 2f;

      Float2D topLeft = new Float2D(0, -halfHeight);
      Float2D bottomRight = new Float2D(width, halfHeight);

      float scale = viewport.getScale();
      topLeft.divide(scale);
      bottomRight.divide(scale);

      textDimension = new Float2D(bottomRight);
      textDimension.subtract(topLeft);

      textOffset = topLeft.clone();
    }
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateDimension(graphics, viewport);
    updateGridCache();

    Font font = viewport.getFont(size * viewport.getZoom(), bold);

    float factor = viewport.getZoom() * viewport.getScale();
    float width = textDimension.getX() * factor;
    float height = textDimension.getY() * factor;
    float halfHeight = height / 3.5f;  //Some magic number.  Not half the height.
    float degrees;

    float widthAdjust = getWidthAdjust() * factor;

    Rotation rotation = getRotation();
    if (rotation.isNorthSouth() || rotation.isCannot())
    {
      degrees = 0;
    }
    else
    {
      degrees = 90;
    }

    float xOffset = Float.NaN;
    float yOffset = Float.NaN;

    if (rotation.isNorth() || rotation.isCannot())
    {
      xOffset = 0 + widthAdjust;
      yOffset = halfHeight;
    }
    else if (rotation.isSouth())
    {
      xOffset = -width + widthAdjust;
      yOffset = halfHeight;
    }
    else if (rotation.isEast())
    {
      xOffset = -halfHeight;
      yOffset = -width + widthAdjust;
    }
    else if (rotation.isWest())
    {
      xOffset = -halfHeight;
      yOffset = 0 + widthAdjust;
    }

    AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(degrees));
    font = font.deriveFont(affineTransform);
    graphics.setFont(font);

    Tuple2 transformedPosition = gridCache.getTransformedPosition();
    int x = viewport.transformGridToScreenSpaceX(transformedPosition);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition);

    graphics.drawString(text, x + xOffset, y + yOffset);
  }

  public float getWidthAdjust()
  {
    float width = textDimension.getX();

    float widthAdjust = 0.1f;
    if (alignment == CENTER)
    {
      widthAdjust += -width / 2;
    }
    else if (alignment == RIGHT)
    {
      widthAdjust += -width;
    }
    return widthAdjust;
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

  public void setParameters(boolean useHolderRotation,
                            Rotation rotation)
  {
    this.useHolderRotation = useHolderRotation;
    this.rotation = rotation;

    invalidateCache();
  }

  public void setPositionRelativeToIC(Tuple2 positionRelativeToIC)
  {
    this.positionRelativeToIC = positionRelativeToIC;

    invalidateCache();
  }

  public Tuple2 getTextDimension()
  {
    return textDimension;
  }

  public Float2D getTextOffset()
  {
    return textOffset;
  }
}

