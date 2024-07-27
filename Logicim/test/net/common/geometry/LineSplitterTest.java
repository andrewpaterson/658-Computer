package net.common.geometry;

import net.logicim.assertions.Validator;
import net.common.type.Int2D;
import net.common.type.Positions;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LineSplitterTest
{
  protected static void testCrossPattern()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(-5, 5, 0));
    lines.add(verticalLine(-5, 0, 0));
    lines.add(verticalLine(0, 5, 0));
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Validator.validate(2, mergedLines.size());

    Positions positionMap = new Positions(lines);
    List<Int2D> positions = positionMap.calculateInt2Ds();
    Validator.validate(5, positions.size());

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    Set<Int2D> junctions = LineSplitter.calculateJunctions(positionMap, lineCache);
    Validator.validate(1, junctions.size());
    Validator.validateEquals(new Int2D(0, 0), junctions.iterator().next());

    Set<Line> splitLines = LineSplitter.split(mergedLines, positionMap);
    Validator.validate(4, splitLines.size());
  }

  protected static void testTeePattern()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(-5, 5, 0));
    lines.add(verticalLine(-5, 0, 0));
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Validator.validate(2, mergedLines.size());

    Positions positionMap = new Positions(lines);
    List<Int2D> positions = positionMap.calculateInt2Ds();
    Validator.validate(4, positions.size());

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    Set<Int2D> junctions = LineSplitter.calculateJunctions(positionMap, lineCache);
    Validator.validate(1, junctions.size());
    Validator.validateEquals(new Int2D(0, 0), junctions.iterator().next());

    Set<Line> splitLines = LineSplitter.split(mergedLines, positionMap);
    Validator.validate(3, splitLines.size());
  }

  protected static void testRightAngle()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(-5, 0, 0));
    lines.add(verticalLine(-5, 0, 0));
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Validator.validate(2, mergedLines.size());

    Positions positionMap = new Positions(lines);
    List<Int2D> positions = positionMap.calculateInt2Ds();
    Validator.validate(3, positions.size());

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    Set<Int2D> junctions = LineSplitter.calculateJunctions(positionMap, lineCache);
    Validator.validate(0, junctions.size());

    Set<Line> splitLines = LineSplitter.split(mergedLines, positionMap);
    Validator.validate(2, splitLines.size());
  }

  protected static Line horizontalLine(int start, int end, int y)
  {
    return new Line(new Int2D(start, y), new Int2D(end, y));
  }

  protected static Line verticalLine(int start, int end, int x)
  {
    return new Line(new Int2D(x, start), new Int2D(x, end));
  }

  public static void test()
  {
    testCrossPattern();
    testTeePattern();
    testRightAngle();
  }
}

