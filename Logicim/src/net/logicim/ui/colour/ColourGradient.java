package net.logicim.ui.colour;

import java.awt.*;

public class ColourGradient
{
  public static void generate(Color[] colors, Object... o)
  {
    for (int i = 0; i < (o.length / 2) - 1; i++)
    {
      Color start = (Color) o[i * 2];
      Color end = (Color) o[i * 2 + 2];
      Integer startIndex = (Integer) o[i * 2 + 1];
      Integer endIndex = (Integer) o[i * 2 + 3];

      generate(start, end, colors, startIndex, endIndex);
    }
  }

  public static void generate(Color start, Color end, Color[] colors, int startIndex, int endIndex)
  {
    for (int i = startIndex; i <= endIndex; i++)
    {
      float f = (float) (i - startIndex) / (float) (endIndex - startIndex);
      colors[i] = new Color(interpolate(f, start.getRed(), end.getRed()),
                            interpolate(f, start.getGreen(), end.getGreen()),
                            interpolate(f, start.getBlue(), end.getBlue()),
                            interpolate(f, start.getAlpha(), end.getAlpha()));
    }
  }

  private static int interpolate(float f, int startChannel, int endChannel)
  {
    return startChannel + (int) ((endChannel - startChannel) * f);
  }
}

