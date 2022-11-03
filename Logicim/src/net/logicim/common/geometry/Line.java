package net.logicim.common.geometry;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;

public class Line
{
  protected Int2D start;
  protected Int2D end;

  protected Rotation direction;

  public Line(Int2D start, Int2D end)
  {
    this.start = start.clone();
    this.end = end.clone();

    this.direction = Rotation.calculateDirection(start.x,
                                                 start.y,
                                                 end.x,
                                                 end.y);
  }

  public static Line createLine(Int2D start, Int2D end)
  {
    if ((start != null) && (end != null))
    {
      if (((start.x == end.x) || (start.y == end.y)) && !((start.x == end.x) && (start.y == end.y)))
      {
        return new Line(start, end);
      }
    }
    return null;
  }

  public Int2D getStart()
  {
    return start;
  }

  public Int2D getEnd()
  {
    return end;
  }

  public Rotation getDirection()
  {
    return direction;
  }

  public int getMinimumY()
  {
    if (start.y < end.y)
    {
      return start.y;
    }
    else
    {
      return end.y;
    }
  }

  public int getMinimumX()
  {
    if (start.x < end.x)
    {
      return start.x;
    }
    else
    {
      return end.x;
    }
  }
  public int getMaximumY()
  {
    if (start.y > end.y)
    {
      return start.y;
    }
    else
    {
      return end.y;
    }
  }

  public int getMaximumX()
  {
    if (start.x > end.x)
    {
      return start.x;
    }
    else
    {
      return end.x;
    }
  }
}

