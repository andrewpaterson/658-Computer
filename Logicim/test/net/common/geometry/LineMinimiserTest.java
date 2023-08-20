package net.common.geometry;

import net.logicim.assertions.Validator;
import net.common.type.Int2D;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class LineMinimiserTest
{
  protected static void testEdgesOverlapping()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(1, 5, 0));
    lines.add(horizontalLine(5, 10, 0));
    lines = LineMinimiser.minimise(lines);

    Validator.validate(1, lines.size());
    Line line = lines.iterator().next();
    Validator.validateEquals(new Int2D(1, 0), line.getStart());
    Validator.validateEquals(new Int2D(10, 0), line.getEnd());
  }

  protected static void testAllOverlapping()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(1, 5, 0));
    lines.add(horizontalLine(2, 4, 0));
    lines.add(horizontalLine(5, 11, 0));
    lines.add(horizontalLine(8, 13, 0));
    lines.add(horizontalLine(10, 16, 0));
    lines = LineMinimiser.minimise(lines);

    Validator.validate(1, lines.size());
    Line line = lines.iterator().next();
    Validator.validateEquals(new Int2D(1, 0), line.getStart());
    Validator.validateEquals(new Int2D(16, 0), line.getEnd());
  }

  protected static void testSomeOverlapping()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(1, 5, 0));
    lines.add(horizontalLine(1, 5, 0));
    lines.add(horizontalLine(8, 13, 0));
    lines.add(horizontalLine(10, 16, 0));
    lines = LineMinimiser.minimise(lines);

    Validator.validate(2, lines.size());
    Iterator<Line> iterator = lines.iterator();
    Line line = iterator.next();
    Validator.validateEquals(new Int2D(1, 0), line.getStart());
    Validator.validateEquals(new Int2D(5, 0), line.getEnd());
    line = iterator.next();
    Validator.validateEquals(new Int2D(8, 0), line.getStart());
    Validator.validateEquals(new Int2D(16, 0), line.getEnd());
  }

  protected static void testParallelWithOverlapping()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(1, 5, 0));
    lines.add(horizontalLine(2, 4, 0));
    lines.add(horizontalLine(5, 11, 0));
    lines.add(horizontalLine(8, 13, 0));
    lines.add(horizontalLine(10, 16, 0));
    lines.add(horizontalLine(1, 5, 1));
    lines.add(horizontalLine(5, 10, 1));
    lines = LineMinimiser.minimise(lines);

    Validator.validate(2, lines.size());
    Iterator<Line> iterator = lines.iterator();
    Line line = iterator.next();
    Validator.validateEquals(new Int2D(1, 0), line.getStart());
    Validator.validateEquals(new Int2D(16, 0), line.getEnd());
    line = iterator.next();
    Validator.validateEquals(new Int2D(1, 1), line.getStart());
    Validator.validateEquals(new Int2D(10, 1), line.getEnd());
  }

  protected static void testOrthogonal()
  {
    Set<Line> lines = new LinkedHashSet<>();
    lines.add(horizontalLine(-5, 5, 0));
    lines.add(verticalLine(-5, 0, 0));
    lines.add(verticalLine(0, 5, 0));
    lines = LineMinimiser.minimise(lines);

    Validator.validate(2, lines.size());
    Iterator<Line> iterator = lines.iterator();
    Line line = iterator.next();
    Validator.validateEquals(new Int2D(-5, 0), line.getStart());
    Validator.validateEquals(new Int2D(5, 0), line.getEnd());
    line = iterator.next();
    Validator.validateEquals(new Int2D(0, -5), line.getStart());
    Validator.validateEquals(new Int2D(0, 5), line.getEnd());
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
    testEdgesOverlapping();
    testAllOverlapping();
    testSomeOverlapping();
    testParallelWithOverlapping();
    testOrthogonal();
  }
}

