package net.logicim.common.geometry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LineMinimiser
{
  public static Set<Line> minimise(Set<Line> inputLines)
  {
    List<LineBucket> lineBuckets = new ArrayList<>();
    for (Line line : inputLines)
    {
      lineBuckets.add(new LineBucket(line));
    }

    for (int i = 0; i < lineBuckets.size(); i++)
    {
      LineBucket lineBucket = lineBuckets.get(i);
      for (int j = i + 1; j < lineBuckets.size(); j++)
      {
        LineBucket otherLineBucket = lineBuckets.get(j);
        if (lineBucket.overlaps(otherLineBucket))
        {
          lineBucket.merge(otherLineBucket);
          lineBuckets.remove(j);
          i--;
          break;
        }
      }
    }

    Set<Line> lines = new LinkedHashSet<>();
    for (LineBucket lineBucket : lineBuckets)
    {
      lines.add(lineBucket.getLine());
    }
    return lines;
  }
}

