package net.logicim.ui.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Strokes
{
  protected Map<Float, Stroke> strokes;

  public Strokes()
  {
    strokes = new HashMap<>();
  }

  public Stroke getStroke(float width)
  {
    Stroke stroke = strokes.get(width);
    if (stroke == null)
    {
      stroke = new BasicStroke(width);
      strokes.put(width, stroke);
    }
    return stroke;
  }
}

