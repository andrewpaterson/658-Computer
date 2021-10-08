package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;

import java.util.ArrayList;
import java.util.List;

public class Tickables
{
  protected List<Tickable> tickables;

  public Tickables()
  {
    this.tickables = new ArrayList<>();
  }

  public void add(Tickable tickable)
  {
    tickables.add(tickable);
  }

  public void run()
  {
    for (Tickable tickable : tickables)
    {
      tickable.resetConnections();
      tickable.startPropagation();
    }

    int count = 0;
    boolean settled;
    do
    {
      System.out.println("-----------------------------");

      settled = true;
      for (Tickable tickable : tickables)
      {
        List<TraceValue> oldTraceValues = tickable.getTraceValues();
        tickable.propagate();
        tickable.updateConnections();

        System.out.println(tickable.getClass().getSimpleName());
        List<TraceValue> newTraceValues = tickable.getTraceValues();
        for (TraceValue newTraceValue : newTraceValues)
        {
          System.out.println(newTraceValue);
        }
        settled &= areSettled(oldTraceValues, newTraceValues);
        System.out.println(settled);
        System.out.println();
      }

      if (!settled)
      {
        for (Tickable tickable : tickables)
        {
          tickable.undoPropagation();
        }
      }
      System.out.println(count);
      count++;
    }
    while (!settled);

    for (Tickable tickable : tickables)
    {
      tickable.donePropagation();
    }
  }

  private static boolean areSettled(List<TraceValue> oldTraceValues, List<TraceValue> newTraceValues)
  {
    int length = oldTraceValues.size();
    for (int i = 0; i < length; i++)
    {
      if (oldTraceValues.get(i) != newTraceValues.get(i))
      {
        return false;
      }
    }
    return true;
  }
}
