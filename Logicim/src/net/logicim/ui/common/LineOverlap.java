package net.logicim.ui.common;

import net.common.geometry.Line;

public enum LineOverlap
{
  Fully,
  Start,  //partially overlapped from the start position but not all the way to the end position
  End,
  Center, //overlapped somewhere in the center including neither the start nor the end position.
  Orthogonal,
  None;

  public static int getOverlap(Line line1, Line line2)
  {
    if (line1.isNorthSouth() && line2.isNorthSouth())
    {
      if (line1.getX() == line2.getX())
      {
        return getOverlap(line1.getMinimumY(), line1.getMaximumY(),
                          line2.getMinimumY(), line2.getMaximumY());
      }
    }
    else if (line1.isEastWest() && line2.isEastWest())
    {
      if (line1.getY() == line2.getY())
      {
        return getOverlap(line1.getMinimumX(), line1.getMaximumX(),
                          line2.getMinimumX(), line2.getMaximumX());
      }
    }
    return 0;
  }

  public static int getOverlap(int minimum1,
                               int maximum1,
                               int minimum2,
                               int maximum2)
  {
    if ((minimum2 <= maximum1) && (maximum2 >= maximum1))
    {
      return maximum1 - minimum2;
    }

    if ((minimum2 <= minimum1) && (maximum2 >= minimum1))
    {
      return maximum2 - minimum1;
    }

    if ((minimum1 <= minimum2) && (maximum1 >= maximum2))
    {
      return maximum2 - minimum2;
    }

    if ((minimum2 <= minimum1) && (maximum2 >= maximum1))
    {
      return maximum1 - maximum2;
    }

    return 0;
  }
}

