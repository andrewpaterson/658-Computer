package net.logicim.common.geometry;

import java.util.LinkedHashSet;
import java.util.Set;

import static net.logicim.ui.common.LineOverlap.None;

public class LineBucket
{
  protected Line line;
  protected Set<Line> lines;

  public LineBucket(Line line)
  {
    this.line = new Line(line);
    this.lines = new LinkedHashSet<>();
    this.lines.add(line);
  }

  public void merge(LineBucket lineBucket)
  {
    this.lines.addAll(lineBucket.getLines());
    line.extend(lineBucket.getLine());
    lineBucket.empty();
  }

  private void empty()
  {
    line = null;
    lines = null;
  }

  public Line getLine()
  {
    return line;
  }

  public Set<Line> getLines()
  {
    return lines;
  }

  public boolean overlaps(LineBucket otherLineBucket)
  {
    return line.getOverlap(otherLineBucket.getLine(), true) != None;
  }
}

