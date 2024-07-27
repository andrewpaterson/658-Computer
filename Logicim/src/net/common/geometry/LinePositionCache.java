package net.common.geometry;

import net.common.type.Int2D;

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
        addVerticalLine(line);
      }
      if (line.isEastWest())
      {
        addHorizontalLine(line);
      }
    }
  }

  protected void addVerticalLine(Line line)
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

  protected void addHorizontalLine(Line line)
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

  public void split(Int2D p)
  {
    int x = p.x;
    int y = p.y;

    Line verticalLine = getVerticalLine(x, y);
    if (verticalLine != null)
    {
      int minimumY = verticalLine.getMinimumY();
      int maximumY = verticalLine.getMaximumY();
      if (y > minimumY && y < maximumY)
      {
        removeVerticalLine(verticalLine);
        Line line1 = new Line(new Int2D(x, minimumY), new Int2D(x, y));
        Line line2 = new Line(new Int2D(x, y), new Int2D(x, maximumY));
        addVerticalLine(line1);
        addVerticalLine(line2);
      }
    }

    Line horizontalLine = getHorizontalLine(x, y);
    if (horizontalLine != null)
    {
      int minimumX = horizontalLine.getMinimumX();
      int maximumX = horizontalLine.getMaximumX();
      if (x > minimumX && x < maximumX)
      {
        removeHorizontalLine(horizontalLine);
        Line line1 = new Line(new Int2D(minimumX, y), new Int2D(x, y));
        Line line2 = new Line(new Int2D(x, y), new Int2D(maximumX, y));
        addHorizontalLine(line1);
        addHorizontalLine(line2);
      }
    }
  }

  private void removeVerticalLine(Line line)
  {
    int x = line.getX();
    List<Line> lines = verticalLines.get(x);
    lines.remove(line);
  }

  private void removeHorizontalLine(Line line)
  {
    int y = line.getY();
    List<Line> lines = horizontalLines.get(y);
    lines.remove(line);
  }

  protected Line getHorizontalLine(int x, int y)
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

  protected Line getVerticalLine(int x, int y)
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

  public Set<Line> getLines()
  {
    Set<Line> result = new LinkedHashSet<>();
    for (List<Line> lines : horizontalLines.values())
    {
      result.addAll(lines);
    }
    for (List<Line> lines : verticalLines.values())
    {
      result.addAll(lines);
    }
    return result;
  }

  public boolean touchesLine(int x, int y)
  {
    Line verticalLine = getVerticalLine(x, y);
    if (verticalLine != null)
    {
      return true;
    }

    Line horizontalLine = getHorizontalLine(x, y);
    if (horizontalLine != null)
    {
      return true;
    }

    return false;
  }
}

