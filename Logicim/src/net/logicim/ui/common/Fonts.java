package net.logicim.ui.common;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fonts
{
  protected Map<Float, Map<Float, Font>> plain;
  protected Map<Float, Map<Float, Font>> bold;

  protected Font defaultGraphicsFont;

  public Fonts()
  {
    plain = new LinkedHashMap<>();
    bold = new LinkedHashMap<>();
  }

  public void ensureDefaultFont(Font font)
  {
    if (defaultGraphicsFont == null)
    {
      defaultGraphicsFont = font;
    }
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
}

