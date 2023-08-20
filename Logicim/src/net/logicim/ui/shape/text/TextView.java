package net.logicim.ui.shape.text;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Tuple2;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.ui.common.*;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.shape.common.ShapeView;
import net.logicim.ui.shape.point.PointGridCache;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import static net.logicim.data.integratedcircuit.decorative.HorizontalAlignment.*;

public class TextView
    extends ShapeView
{
  protected Tuple2 positionRelativeToIC;
  protected Float2D textOffset;
  protected Float2D textDimension;

  protected String text;
  protected String fontName;
  protected float size;
  protected boolean bold;
  protected HorizontalAlignment alignment;
  protected Color color;

  protected PointGridCache gridCache;

  public TextView(ShapeHolder shapeHolder,
                  Tuple2 positionRelativeToIC,
                  String text,
                  String fontName,
                  float size,
                  boolean bold,
                  HorizontalAlignment alignment)
  {
    super(shapeHolder);
    this.positionRelativeToIC = positionRelativeToIC;
    this.fontName = fontName;
    this.alignment = alignment;

    this.text = text;
    this.size = size;
    this.bold = bold;
    this.color = Colours.getInstance().getText();

    this.gridCache = new PointGridCache(positionRelativeToIC);

    calculateDimension();
  }

  public static void centerHorizontally(TextView... textViews)
  {
    float height = 0;
    float maximumTextOffset = 0;
    for (TextView textView : textViews)
    {
      if (textView != null)
      {
        height += textView.getTextDimension().getY();
        float textOffset = -textView.getTextOffset().getY();
        if (textOffset > maximumTextOffset)
        {
          maximumTextOffset = textOffset;
        }
      }
    }

    float offset = -height / 2 + maximumTextOffset;
    for (TextView textView : textViews)
    {
      if (textView != null)
      {
        textView.setPositionRelativeToIC(new Float2D(offset, textView.getTextOffset().getX()));
        offset += textView.getTextDimension().getY();
      }
    }
  }

  public void updateGridCache()
  {
    if (!gridCache.isValid())
    {
      gridCache.update(positionRelativeToIC,
                       getShapeHolderRotation(),
                       shapeHolder.getPosition());
    }
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public void calculateDimension()
  {
    Font font = getFont();

    this.textOffset = calculateTextOffset(font, text);
    this.textDimension = calculateTextDimension(font, text);
  }

  public static Float2D calculateTextOffset(Font font, String text)
  {
    FontRenderContext fontRenderContext = Fonts.getInstance().getFontRenderContext();

    Rectangle2D stringBounds = font.getStringBounds(text, fontRenderContext);
    int height = (int) stringBounds.getHeight();
    int width = (int) stringBounds.getWidth();
    float halfHeight = (float) height / 2f;

    Float2D topLeft = new Float2D(0, -halfHeight);
    Float2D bottomRight = new Float2D(width, halfHeight);

    float scale = Viewport.getScale();
    topLeft.divide(scale);
    bottomRight.divide(scale);

    Float2D textDimension = new Float2D(bottomRight);
    textDimension.subtract(topLeft);

    return topLeft.clone();
  }

  public static Float2D calculateTextDimension(Font font, String text)
  {
    FontRenderContext fontRenderContext = Fonts.getInstance().getFontRenderContext();

    Rectangle2D stringBounds = font.getStringBounds(text, fontRenderContext);
    int height = (int) stringBounds.getHeight();
    int width = (int) stringBounds.getWidth();
    float halfHeight = (float) height / 2f;

    Float2D topLeft = new Float2D(0, -halfHeight);
    Float2D bottomRight = new Float2D(width, halfHeight);

    float scale = Viewport.getScale();
    topLeft.divide(scale);
    bottomRight.divide(scale);

    Float2D textDimension = new Float2D(bottomRight);
    textDimension.subtract(topLeft);

    return textDimension;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    updateGridCache();

    float factor = viewport.getScaledZoom();
    float width = textDimension.getX() * factor;
    float height = textDimension.getY() * factor;
    float halfHeight = height / 3.5f;  //Some magic number.  Not half the height.
    float degrees;

    float widthAdjust = getWidthAdjust(0.1f) * factor;

    Rotation rotation = shapeHolder.getRotation();
    rotation = rotation.rotateRight(relativeRightRotations);
    if (rotation.isNorthSouth() || rotation.isCannot())
    {
      degrees = 90;
    }
    else
    {
      degrees = 0;
    }

    float xOffset = Float.NaN;
    float yOffset = Float.NaN;

    if (rotation.isNorth() || rotation.isCannot())
    {
      xOffset = -halfHeight;
      yOffset = -width - widthAdjust;
    }
    else if (rotation.isSouth())
    {
      xOffset = -halfHeight;
      yOffset = 0 + widthAdjust;
    }
    else if (rotation.isEast())
    {
      xOffset = -width - widthAdjust;
      yOffset = halfHeight;
    }
    else if (rotation.isWest())
    {
      xOffset = 0 + widthAdjust;
      yOffset = halfHeight;
    }

    Font font = Fonts.getInstance().getFont(fontName, degrees, size * viewport.getZoom(), bold);
    graphics.setFont(font);

    Tuple2 transformedPosition = gridCache.getTransformedPosition();
    int x = viewport.transformGridToScreenSpaceX(transformedPosition);
    int y = viewport.transformGridToScreenSpaceY(transformedPosition);

    graphics.setColor(color);
    graphics.drawString(text, x + xOffset, y + yOffset);
  }

  public float getWidthAdjust(float offset)
  {
    float width = textDimension.getX();

    if (alignment == LEFT)
    {
      return offset;
    }
    if (alignment == CENTER)
    {
      return offset - width / 2;
    }
    else if (alignment == RIGHT)
    {
      return offset - width;
    }
    else
    {
      throw new SimulatorException("Cannot calculate width adjustment.");
    }
  }

  @Override
  public void boundingBoxInclude(BoundingBox boundingBox)
  {
    BoundingBox localBox = new BoundingBox();
    localBox.include(calculateTopLeft());
    localBox.include(calculateBottomRight());
    transformAndIncludeLocalBox(boundingBox, localBox);
  }

  @Override
  public void invalidateCache()
  {
    gridCache.invalidate();
  }

  public void setPositionRelativeToIC(Tuple2 positionRelativeToIC)
  {
    this.positionRelativeToIC = positionRelativeToIC;

    invalidateCache();
  }

  public void setText(String text)
  {
    this.text = text;
    calculateDimension();
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

  public Font getFont()
  {
    return Fonts.getInstance().getFont(fontName, 0, size, bold);
  }

  public Float2D calculateTopLeft()
  {
    Float2D topLeft = getTextOffset().clone();
    Float2D bottomRight = new Float2D(getTextDimension());
    float height = bottomRight.y;
    bottomRight.add(topLeft);

    float widthAdjust = getWidthAdjust(0.0f);
    topLeft.x += widthAdjust - height / 4f;
    bottomRight.x += widthAdjust + height / 4f;

    Rotation.East.rotate(topLeft, topLeft);
    topLeft.add(positionRelativeToIC);

    return topLeft;
  }

  public Float2D calculateBottomRight()
  {
    Float2D topLeft = getTextOffset().clone();
    Float2D bottomRight = new Float2D(getTextDimension());
    float height = bottomRight.y;
    bottomRight.add(topLeft);

    float widthAdjust = getWidthAdjust(0.0f);
    bottomRight.x += widthAdjust + height / 4f;

    Rotation.East.rotate(bottomRight, bottomRight);
    bottomRight.add(positionRelativeToIC);

    return bottomRight;
  }
}

