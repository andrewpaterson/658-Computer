package net.logicim.ui.common;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fonts
{
  protected Map<Float, Font> plain;
  protected Map<Float, Font> bold;

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

  public Font getBoldFont(float size)
  {
    Font font = bold.get(size);
    if (font == null)
    {
      font = defaultGraphicsFont.deriveFont(Font.BOLD, size);
      bold.put(size, font);
    }
    return font;
  }

  public Font getPlainFont(float size)
  {
    Font font = plain.get(size);
    if (font == null)
    {
      font = defaultGraphicsFont.deriveFont(Font.PLAIN, size);
      plain.put(size, font);
    }
    return font;
  }
}

