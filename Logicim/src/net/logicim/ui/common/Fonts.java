package net.logicim.ui.common;

import net.common.SimulatorException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.awt.Font.SANS_SERIF;

//  Sub-circuit instances, and so the positions of their pins, are sized from measured text.  Java maps SansSerif to a
//  different font on each platform (Arial on Windows, usually DejaVu Sans on Linux), so SansSerif is loaded from the
//  bundled Liberation Sans, which has the same metrics as Arial, and text is measured without any device transform or
//  anti-aliasing.  This keeps circuit layouts the same on every platform.
public class Fonts
{
  public static final String BUNDLED_FONTS_PATH = "res/fonts";

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
      fontRenderContext = new FontRenderContext(null, false, false);

      defaultFont = getFont(SANS_SERIF);

      for (int i = 1; i <= 20; i++)
      {
        getPlainFont(0, i);
        getBoldFont(0, i);
      }
    }
  }

  protected void ensureDefaultFont()
  {
    if (defaultGraphicsFont == null)
    {
      Graphics2D graphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
      ensureDefaultFont(graphics);
      graphics.dispose();
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
    ensureDefaultFont();
    NamedFont namedFont = fonts.get(fontName);
    if (namedFont == null)
    {
      namedFont = createNamedFont(fontName);
      fonts.put(fontName, namedFont);
    }
    return namedFont;
  }

  protected NamedFont createNamedFont(String fontName)
  {
    if (fontName.equals(SANS_SERIF))
    {
      float size = defaultGraphicsFont.getSize2D();
      Font plainFont = loadBundledFont("LiberationSans-Regular.ttf").deriveFont(defaultGraphicsFont.getStyle(), size);
      Font boldFont = loadBundledFont("LiberationSans-Bold.ttf").deriveFont(size);
      return new NamedFont(plainFont, boldFont);
    }
    else
    {
      Font font = new Font(fontName, defaultGraphicsFont.getStyle(), defaultGraphicsFont.getSize());
      return new NamedFont(font);
    }
  }

  protected Font loadBundledFont(String fileName)
  {
    File file = new File(BUNDLED_FONTS_PATH, fileName);
    try
    {
      return Font.createFont(Font.TRUETYPE_FONT, file);
    }
    catch (FontFormatException | IOException e)
    {
      throw new SimulatorException("Cannot load font [%s].  Logicim must be run from its module directory.  %s", file.getAbsolutePath(), e.getMessage());
    }
  }

  public Font getBoldFont(float rotation, float size)
  {
    ensureDefaultFont();
    return defaultFont.getBoldFont(rotation, size);
  }

  public Font getPlainFont(float rotation, float size)
  {
    ensureDefaultFont();
    return defaultFont.getPlainFont(rotation, size);
  }

  public Font getFont(float size, boolean bold)
  {
    ensureDefaultFont();
    return defaultFont.getFont(0, size, bold);
  }

  public Font getFont(String fontName, float degrees, float size, boolean bold)
  {
    NamedFont namedFont = getFont(fontName);
    return namedFont.getFont(degrees, size, bold);
  }

  public FontRenderContext getFontRenderContext()
  {
    ensureDefaultFont();
    return fontRenderContext;
  }
}

