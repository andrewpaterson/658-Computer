package net.logicim.common.type;

import net.logicim.common.geometry.Line;

import java.util.*;

public class Positions
{
  protected Map<Integer, Set<Integer>> xes;

  public Positions(Set<Line> lines)
  {
    xes = new LinkedHashMap<>();
    for (Line line : lines)
    {
      add(line.getStart());
      add(line.getEnd());
    }
  }

  private void add(Int2D p)
  {
    Set<Integer> ys = xes.get(p.x);
    if (ys == null)
    {
      ys = new LinkedHashSet<>();
      xes.put(p.x, ys);
    }
    ys.add(p.y);
  }

  public Map<Integer, Set<Integer>> getXes()
  {
    return xes;
  }

  public List<Int2D> calculateInt2Ds()
  {
    List<Int2D> positions = new ArrayList<>();
    List<Integer> xes = new ArrayList<>(this.xes.keySet());
    Collections.sort(xes);
    for (Integer x : xes)
    {
      List<Integer> ys = new ArrayList<>(this.xes.get(x));
      Collections.sort(ys);
      for (Integer y : ys)
      {
        positions.add(new Int2D(x, y));
      }
    }
    return positions;
  }
}

