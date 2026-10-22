package net.logicim.ui.common;

import net.common.type.Tuple2;
import net.logicim.ui.shape.text.TextView;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static java.awt.Font.SANS_SERIF;
import static org.junit.jupiter.api.Assertions.assertEquals;

//  Sub-circuit instance layouts are sized from SansSerif text, so these measurements must be the same on every
//  platform.  They are the sizes Arial gives on Windows, where the saved circuits were drawn.
public class FontsTest
{
  @Test
  public void testSansSerifIsBundledLiberationSans()
  {
    assertEquals("Liberation Sans", Fonts.getInstance().getFont(SANS_SERIF, 0, 10, false).getFontName());
    assertEquals("Liberation Sans Bold", Fonts.getInstance().getFont(SANS_SERIF, 0, 13, true).getFontName());
  }

  @Test
  public void testSansSerifTextDimensions()
  {
    assertTextDimension("Intermid", 13, true, 5.3f, 1.4f);
    assertTextDimension("Subcircuit", 13, true, 6.4f, 1.4f);
    assertTextDimension("In", 10, false, 0.9f, 1.1f);
    assertTextDimension("Out And", 10, false, 3.9f, 1.1f);
    assertTextDimension("WWWWWWWW", 10, false, 7.2f, 1.1f);
  }

  protected static void assertTextDimension(String text, float size, boolean bold, float expectedWidth, float expectedHeight)
  {
    Font font = Fonts.getInstance().getFont(SANS_SERIF, 0, size, bold);
    Tuple2 dimension = TextView.calculateTextDimension(font, text);
    assertEquals(expectedWidth, dimension.getX(), 0.001f, text + " width");
    assertEquals(expectedHeight, dimension.getY(), 0.001f, text + " height");
  }
}
