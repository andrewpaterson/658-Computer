package net.logicim.ui.common;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.Font.SANS_SERIF;

public class Fonts
{
  protected Map<String, NamedFont> fonts;

  protected Font defaultGraphicsFont;
  protected NamedFont defaultFont;
  protected FontRenderContext fontRenderContext;

  private static Fonts instance = null;

  public Fonts()
  {
    fonts = new LinkedHashMap<>();
  }

  public void ensureDefaultFont(Graphics2D graphics)
  {
    if (defaultGraphicsFont == null)
    {
      defaultGraphicsFont = graphics.getFont();
      fontRenderContext = graphics.getFontRenderContext();

      defaultFont = getFont(SANS_SERIF);

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

  private NamedFont getFont(String fontName)
  {
    NamedFont namedFont = fonts.get(fontName);
    if (namedFont == null)
    {
      Font font = new Font(fontName, defaultGraphicsFont.getStyle(), defaultGraphicsFont.getSize());
      namedFont = new NamedFont(font);
      fonts.put(fontName, namedFont);
    }
    return namedFont;
  }

  public Font getBoldFont(float rotation, float size)
  {
    return defaultFont.getBoldFont(rotation, size);
  }

  public Font getPlainFont(float rotation, float size)
  {
    return defaultFont.getPlainFont(rotation, size);
  }

  public Font getFont(float size, boolean bold)
  {
    return defaultFont.getFont(0, size, bold);
  }

  public Font getFont(String fontName, float degrees, float size, boolean bold)
  {
    NamedFont namedFont = getFont(fontName);
    return namedFont.getFont(degrees, size, bold);
  }

  public FontRenderContext getFontRenderContext()
  {
    return fontRenderContext;
  }
}

