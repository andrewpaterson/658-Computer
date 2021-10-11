package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;

import java.util.ArrayList;
import java.util.List;

public class Tickables
{
  protected List<Tickable> tickables;
  protected boolean debug;

  public Tickables(boolean debug)
  {
    this.tickables = new ArrayList<>();
    this.debug = debug;
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
      if (count == 200)
      {
        break;
      }

      settled = true;
      for (Tickable tickable : tickables)
      {
        List<TraceValue> oldTraceValues = tickable.getTraceValues();

        tickable.propagate();
        debugLog(tickable.toDebugString());
        debugLog("");

        tickable.updateConnections();
        List<TraceValue> newTraceValues = tickable.getTraceValues();
        settled &= areSettled(oldTraceValues, newTraceValues);

      }

      if (!settled)
      {
        for (Tickable tickable : tickables)
        {
          tickable.undoPropagation();
        }
      }
      count++;
    }
    while (!settled);

    debugLog("------ Settled in [" + count + "] iterations. ------");
    for (Tickable tickable : tickables)
    {
      tickable.donePropagation();
    }
  }

  private void debugLog(String s)
  {
    if (debug)
    {
      System.out.println(s);
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

