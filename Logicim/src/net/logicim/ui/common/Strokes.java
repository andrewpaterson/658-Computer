package net.logicim.ui.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Strokes
{
  protected Map<Float, Stroke> solidStrokes;

  public Strokes()
  {
    solidStrokes = new HashMap<>();
  }

  public Stroke getSolidStroke(float width)
  {
    Stroke stroke = solidStrokes.get(width);
    if (stroke == null)
    {
      stroke = new BasicStroke(width);
      solidStrokes.put(width, stroke);
    }
    return stroke;
  }
}

