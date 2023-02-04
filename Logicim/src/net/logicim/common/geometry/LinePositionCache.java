package net.logicim.common.geometry;

import java.util.*;

public class LinePositionCache
{
  protected Map<Integer, List<Line>> verticalLines;
  protected Map<Integer, List<Line>> horizontalLines;

  public LinePositionCache(Set<Line> lines)
  {
    verticalLines = new LinkedHashMap<>();
    horizontalLines = new LinkedHashMap<>();

    for (Line line : lines)
    {
      if (line.isNorthSouth())
      {
        int x = line.getX();
        List<Line> verticalList = verticalLines.get(x);
        if (verticalList == null)
        {
          verticalList = new ArrayList<>();
          verticalLines.put(x, verticalList);
        }
        verticalList.add(line);
      }
      if (line.isEastWest())
      {
        int y = line.getY();
        List<Line> horizontalList = horizontalLines.get(y);
        if (horizontalList == null)
        {
          horizontalList = new ArrayList<>();
          horizontalLines.put(y, horizontalList);
        }
        horizontalList.add(line);
      }
    }
  }

  public Map<Integer, List<Line>> getVerticalLines()
  {
    return verticalLines;
  }

  public Map<Integer, List<Line>> getHorizontalLines()
  {
    return horizontalLines;
  }
}

