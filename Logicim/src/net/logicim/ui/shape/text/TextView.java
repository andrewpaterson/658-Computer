package net.logicim.ui.shape.text;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.data.integratedcircuit.decorative.VerticalAlignment;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.rectangle.RectangleGridCache;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class TextView
    extends ShapeView
{
  protected Tuple2 positionRelativeToIC;
  protected Tuple2 dimension;

  protected String text;
  protected float size;
  protected boolean bold;
  protected boolean useHolderRotation;
  protected Rotation rotation;

  protected HorizontalAlignment horizontalAlignment;
  protected VerticalAlignment verticalAlignment;

  protected RectangleGridCache gridCache;

  public TextView(ShapeHolder shapeHolder, Tuple2 positionRelativeToIC, String text, float size, boolean bold)
  {
    this(shapeHolder, positionRelativeToIC, text, size, bold, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
  }

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  float size,
                  boolean bold,
                  HorizontalAlignment horizontalAlignment,
                  VerticalAlignment verticalAlignment)
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
                  HorizontalAlignment horizontalAlignment,
                  VerticalAlignment verticalAlignment)
  {
    super(shapeHolder);
    this.positionRelativeToIC = positionRelativeToIC;
    this.dimension = null;

    this.text = text;
    this.size = size;
    this.bold = bold;
    this.useHolderRotation = useHolderRotation;
    this.rotation = rotation;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;

    this.gridCache = new RectangleGridCache(null, positionRelativeToIC);
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(dimension, positionRelativeToIC, shapeHolder.getRotation(), shapeHolder.getPosition());
    }
  }

  public Tuple2 getDimension()
  {
    return dimension;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateDimension(graphics, viewport);
    updateGridCache();

    Tuple2 transformedPosition = gridCache.getTransformedPosition();
    int x = viewport.transformGridToScreenSpaceX(transformedPosition);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition);

    Font font = viewport.getFont(size * viewport.getZoom(), bold);
    FontMetrics metrics = graphics.getFontMetrics(font);

    float width = dimension.getX() * viewport.getZoom() * viewport.getScale();
    float height = dimension.getY() * viewport.getZoom() * viewport.getScale();
    float xOffset;
    if (horizontalAlignment == HorizontalAlignment.LEFT)
    {
      xOffset = 0;
    }
    else if (horizontalAlignment == HorizontalAlignment.CENTER)
    {
      xOffset = -(width / 2f);
    }
    else
    {
      xOffset = -width;
    }

    float yOffset;
    if (verticalAlignment == VerticalAlignment.TOP)
    {
      yOffset = height;
    }
    else if (verticalAlignment == VerticalAlignment.CENTER)
    {
      yOffset = height / 2f;
    }
    else
    {
      yOffset = 0;
    }

    AffineTransform affineTransform = new AffineTransform();
    affineTransform.translate(xOffset, yOffset);
    affineTransform.rotate(Math.toRadians(getRotation().rotationToDegrees()));
    graphics.setTransform(affineTransform);

    graphics.setFont(font);
    graphics.drawString(text, x, y);
  }

  public void updateDimension(Graphics2D graphics, Viewport viewport)
  {
    if (dimension == null)
    {
      Font font = viewport.getFont(size, bold);
      FontMetrics metrics = graphics.getFontMetrics(font);

      Rectangle2D stringBounds = metrics.getStringBounds(text, graphics);

      Float2D topLeft = new Float2D(stringBounds.getMinX(), stringBounds.getMinY());
      float scale = viewport.getScale();
      float yOffset = (metrics.getAscent()) / scale;
      topLeft.divide(scale);
      topLeft.y += yOffset;
      Float2D bottomRight = new Float2D(stringBounds.getMaxX(), stringBounds.getMaxY());
      bottomRight.divide(scale);
      bottomRight.y += yOffset;

      dimension = new Float2D(bottomRight);
      dimension.subtract(topLeft);
    }
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

  public void setParameters(HorizontalAlignment horizontalAlignment,
                            VerticalAlignment verticalAlignment)
  {
    setParameters(false, Rotation.North, horizontalAlignment, verticalAlignment);
  }

  public void setParameters(boolean useHolderRotation,
                            Rotation rotation,
                            HorizontalAlignment horizontalAlignment,
                            VerticalAlignment verticalAlignment)
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

