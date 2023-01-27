package net.logicim.ui.common;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fonts
{
  protected Map<Float, Map<Float, Font>> plain;
  protected Map<Float, Map<Float, Font>> bold;

  protected Font defaultGraphicsFont;
  protected FontRenderContext fontRenderContext;

  private static Fonts instance = null;

  public Fonts()
  {
    plain = new LinkedHashMap<>();
    bold = new LinkedHashMap<>();
  }

  public void ensureDefaultFont(Graphics2D graphics)
  {
    if (defaultGraphicsFont == null)
    {
      defaultGraphicsFont = graphics.getFont();
      fontRenderContext = graphics.getFontRenderContext();

      for (int i = 1; i <= 20; i++)
      {
        getPlainFont(0, i);
        getBoldFont(0, i);
      }
    }
  }

  public static Fonts getInstance()
  {
    if (instance == null)
    {
      instance = new Fonts();
    }
    return instance;
  }

  public Font getBoldFont(float rotation, float size)
  {
    Map<Float, Font> rotationMap = bold.get(rotation);
    if (rotationMap == null)
    {
      rotationMap = new LinkedHashMap<>();
      bold.put(rotation, rotationMap);
    }

    Font font = rotationMap.get(size);
    if (font == null)
    {
      font = defaultGraphicsFont.deriveFont(Font.BOLD, size);
      if (rotation != 0)
      {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(rotation));
        font = font.deriveFont(affineTransform);
      }
      rotationMap.put(size, font);
    }
    return font;
  }

  public Font getPlainFont(float rotation, float size)
  {
    Map<Float, Font> rotationMap = plain.get(rotation);
    if (rotationMap == null)
    {
      rotationMap = new LinkedHashMap<>();
      plain.put(rotation, rotationMap);
    }

    Font font = rotationMap.get(size);
    if (font == null)
    {
      font = defaultGraphicsFont.deriveFont(Font.PLAIN, size);
      if (rotation != 0)
      {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(rotation));
        font = font.deriveFont(affineTransform);
      }
      rotationMap.put(size, font);
    }
    return font;
  }

  public Font getFont(float size, boolean bold)
  {
    if (bold)
    {
      return getBoldFont(0, size);
    }
    else
    {
      return getPlainFont(0, size);
    }
  }

  public FontRenderContext getFontRenderContext()
  {
    return fontRenderContext;
  }
}

