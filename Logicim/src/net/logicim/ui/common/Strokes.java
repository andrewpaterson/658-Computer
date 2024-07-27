package net.logicim.ui.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Strokes
{
  protected Map<Float, Stroke> solidStrokes;

  private static Strokes instance = null;

  public Strokes()
  {
    solidStrokes = new HashMap<>();
  }

  public static Strokes getInstance()
  {
    if (instance == null)
    {
      instance = new Strokes();
    }
    return instance;
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

