package net.logicim.ui.common;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedHashMap;
import java.util.Map;

public class NamedFont
{
  protected Map<Float, Map<Float, Font>> plain;
  protected Map<Float, Map<Float, Font>> bold;
  protected String name;
  protected Font defaultFont;

  public NamedFont(Font defaultFont)
  {
    this.name = defaultFont.getName();
    this.defaultFont = defaultFont;

    plain = new LinkedHashMap<>();
    bold = new LinkedHashMap<>();
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
      font = defaultFont.deriveFont(Font.BOLD, size);
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
      font = defaultFont.deriveFont(Font.PLAIN, size);
      if (rotation != 0)
      {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(Math.toRadians(rotation));
        font = font.deriveFont(affineTransform);
      }
      rotationMap.put(size, font);
    }
    return font;
  }

  public Font getFont(float rotation, float size, boolean bold)
  {
    if (bold)
    {
      return getBoldFont(rotation, size);
    }
    else
    {
      return getPlainFont(rotation, size);
    }
  }
}

