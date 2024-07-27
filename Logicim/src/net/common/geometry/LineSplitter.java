package net.common.geometry;

import net.common.type.Int2D;
import net.common.type.Positions;

import java.util.*;

public class LineSplitter
{
  public static Set<Line> split(Set<Line> lines, Positions positions)
  {
    return split(new LinePositionCache(lines), positions, new ArrayList<>());
  }

  public static Set<Line> split(LinePositionCache cache, Positions positions, List<Int2D> additionalJunctions)
  {
    Set<Int2D> junctions = calculateJunctions(positions, cache);
    junctions.addAll(additionalJunctions);
    for (Int2D junction : junctions)
    {
      cache.split(junction);
    }

    return cache.getLines();
  }

  protected static Set<Int2D> calculateJunctions(Positions positions, LinePositionCache cache)
  {
    Set<Int2D> junctions = new LinkedHashSet<>();
    Map<Integer, Set<Integer>> xes = positions.getXes();
    for (Map.Entry<Integer, Set<Integer>> entry : xes.entrySet())
    {
      int x = entry.getKey();
      Set<Integer> ys = entry.getValue();
      for (Integer y : ys)
      {
        Line horizontalLine = cache.getHorizontalLine(x, y);
        if (horizontalLine != null)
        {
          Line verticalLine = cache.getVerticalLine(x, y);
          if (verticalLine != null)
          {
            if (isJunction(verticalLine, horizontalLine, x, y))
            {
              junctions.add(new Int2D(x, y));
            }
          }
        }
      }
    }
    return junctions;
  }

  private static boolean isJunction(Line verticalLine, Line horizontalLine, int x, int y)
  {
    if (x > horizontalLine.getMinimumX() && x < horizontalLine.getMaximumX())
    {
      return true;
    }
    if (y > verticalLine.getMinimumY() && y < verticalLine.getMaximumY())
    {
      return true;
    }

    return false;
  }
}
