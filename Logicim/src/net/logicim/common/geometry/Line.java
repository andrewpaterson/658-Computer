package net.logicim.common.geometry;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.LineOverlap;
import net.logicim.ui.common.Rotation;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.common.LineOverlap.*;
import static net.logicim.ui.common.Rotation.*;

public class Line
{
  protected Int2D start;
  protected Int2D end;

  protected Rotation direction;

  public Line(Int2D start, Int2D end)
  {
    this.start = start.clone();
    this.end = end.clone();

    this.direction = Rotation.calculateDirection(this.start.x,
                                                 this.start.y,
                                                 this.end.x,
                                                 this.end.y);
  }

  public Line(Line line)
  {
    this(line.getStart(), line.getEnd());
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

  public static List<Line> lines(Line... lines)
  {
    ArrayList<Line> result = new ArrayList<>(lines.length);
    for (Line line : lines)
    {
      if (line != null)
      {
        result.add(line);
      }
    }
    return result;
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

  public void getCenter(Int2D center)
  {
    center.set(start);
    center.add(end);
    center.divide(2);
  }

  public boolean isPositionOn(Int2D p)
  {
    return isPositionOn(p.x, p.y);
  }

  public boolean isPositionOn(int x, int y)
  {
    boolean positionFallsOnTrace = false;
    if ((direction == North) &&
        (x == start.x) &&
        ((y >= start.y) && (y <= end.y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == South) &&
        (x == start.x) &&
        ((y >= end.y) && (y <= start.y)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == West) &&
        (y == start.y) &&
        ((x >= start.x) && (x <= end.x)))
    {
      positionFallsOnTrace = true;
    }
    if ((direction == Rotation.East) &&
        (y == start.y) &&
        ((x >= end.x) && (x <= start.x)))
    {
      positionFallsOnTrace = true;
    }
    return positionFallsOnTrace;
  }

  public LineOverlap getOverlap(Line otherLine, boolean endInclusive)
  {
    Rotation lineDirection = otherLine.getDirection();
    if (lineDirection == Cannot || this.direction == Cannot)
    {
      return None;
    }
    else
    {
      if ((lineDirection == North || lineDirection == South) &&
          (direction == North || direction == South))
      {
        if (getStart().x == otherLine.getStart().x)
        {
          return calculateOverlap(otherLine.getMinimumY(),
                                  otherLine.getMaximumY(),
                                  getMinimumY(),
                                  getMaximumY(),
                                  getStart().y,
                                  getEnd().y,
                                  endInclusive);
        }
        else
        {
          return None;
        }
      }
      else if ((lineDirection == East || lineDirection == West) &&
               (direction == East || direction == West))
      {
        if (getStart().y == otherLine.getStart().y)
        {
          return calculateOverlap(otherLine.getMinimumX(),
                                  otherLine.getMaximumX(),
                                  getMinimumX(),
                                  getMaximumX(),
                                  getStart().x,
                                  getEnd().x,
                                  endInclusive);
        }
        else
        {
          return None;
        }
      }
      else
      {
        return None;
      }
    }
  }

  public LineOverlap calculateOverlap(int lineMin, int lineMax, int traceMin, int traceMax, int traceStart, int traceEnd, boolean endInclusive)
  {
    if (endInclusive)
    {
      if (lineMax < traceMin)
      {
        return None;
      }
      if (lineMin > traceMax)
      {
        return None;
      }
    }
    else
    {
      if (lineMax <= traceMin)
      {
        return None;
      }
      if (lineMin >= traceMax)
      {
        return None;
      }

    }

    if ((lineMin <= traceMin) && (lineMax >= traceMax))
    {
      return Fully;  //The line fully overlaps the center of the trace.
    }
    if ((lineMin > traceMin) && (lineMax < traceMax))
    {
      return Center;  //The line only overlaps the center of the trace.
    }

    if ((traceStart >= lineMin) && (traceStart <= lineMax))
    {
      return Start;  //The line overlaps the start of the trace (but not the end).
    }
    if ((traceEnd >= lineMin) && (traceEnd <= lineMax))
    {
      return End;  //The line overlaps the end of the trace (but not the start).
    }

    throw new SimulatorException("Could not determine line over trace overlap.");
  }

  public boolean isEastWest()
  {
    return direction == West || direction == East;
  }

  public boolean isNorthSouth()
  {
    return direction == North || direction == South;
  }

  public int getY()
  {
    if (start.y == end.y)
    {
      return start.y;
    }
    else
    {
      throw new SimulatorException("Could get single Y for a North / South line.");
    }
  }

  public int getX()
  {
    if (start.x == end.x)
    {
      return start.x;
    }
    else
    {
      throw new SimulatorException("Could get single X for an East / West line.");
    }
  }

  public void getBoundingBoxInGridSpace(Float2D boundBoxPosition, Float2D boundBoxDimension)
  {
    int y1 = getMinimumY();
    int x1 = getMinimumX();
    int x2 = getMaximumX();
    int y2 = getMaximumY();
    boundBoxPosition.set(x1, y1);
    boundBoxDimension.set(x2 - x1, y2 - y1);
  }

  public void set(Int2D start, Int2D end)
  {
    this.start.set(start);
    this.end.set(end);

    this.direction = Rotation.calculateDirection(this.start.x,
                                                 this.start.y,
                                                 this.end.x,
                                                 this.end.y);
  }

  @Override
  public Line clone()
  {
    return new Line(start, end);
  }

  public void extend(Line otherLine)
  {
    Rotation lineDirection = otherLine.getDirection();
    if (lineDirection != Cannot && this.direction != Cannot)
    {
      if ((lineDirection == North || lineDirection == South) &&
          (direction == North || direction == South))
      {
        if (getStart().x == otherLine.getStart().x)
        {
          int minimumY = getMinimumY();
          int otherMinimumY = otherLine.getMinimumY();
          minimumY = Math.min(minimumY, otherMinimumY);

          int maximumY = getMaximumY();
          int otherMaximumY = otherLine.getMaximumY();
          maximumY = Math.max(maximumY, otherMaximumY);

          start.y = minimumY;
          end.y = maximumY;
          direction = calculateDirection(start.x, start.y, end.x, end.y);
        }
      }
      else if ((lineDirection == East || lineDirection == West) &&
               (direction == East || direction == West))
      {
        if (getStart().y == otherLine.getStart().y)
        {
          int minimumX = getMinimumX();
          int otherMinimumX = otherLine.getMinimumX();
          minimumX = Math.min(minimumX, otherMinimumX);

          int maximumX = getMaximumX();
          int otherMaximumX = otherLine.getMaximumX();
          maximumX = Math.max(maximumX, otherMaximumX);

          start.x = minimumX;
          end.x = maximumX;
          direction = calculateDirection(start.x, start.y, end.x, end.y);
        }
      }
    }
  }
}

