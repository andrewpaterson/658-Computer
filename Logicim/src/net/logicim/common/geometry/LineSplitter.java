package net.logicim.common.geometry;

import net.logicim.common.type.Int2D;
import net.logicim.common.type.Positions;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LineSplitter
{
  public static Set<Line> split(Set<Line> lines, Positions positions)
  {
    LinePositionCache cache = new LinePositionCache(lines);
    Set<Int2D> junctions = calculateJunctions(positions, cache);
    for (Int2D junction : junctions)
    {
      
    }

    return new LinkedHashSet<>();
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
        Map<Integer, List<Line>> horizontalLines = cache.getHorizontalLines();
        Line horizontalLine = getHorizontalLine(horizontalLines, x, y);
        if (horizontalLine != null)
        {
          Map<Integer, List<Line>> verticalLines = cache.getVerticalLines();
          Line verticalLine = getVerticalLine(verticalLines, x, y);
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

  protected static Line getHorizontalLine(Map<Integer, List<Line>> horizontalLines, int x, int y)
  {
    List<Line> horizontalList = horizontalLines.get(y);
    if (horizontalList != null)
    {
      for (Line line : horizontalList)
      {
        if (line.isPositionOn(x, y))
        {
          return line;
        }
      }
    }
    return null;
  }

  protected static Line getVerticalLine(Map<Integer, List<Line>> verticalLines, int x, int y)
  {
    List<Line> verticalList = verticalLines.get(x);
    if (verticalList != null)
    {
      for (Line line : verticalList)
      {
        if (line.isPositionOn(x, y))
        {
          return line;
        }
      }
    }
    return null;
  }
}
