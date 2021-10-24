package net.simulation.common;

import java.util.ArrayList;
import java.util.List;

import static net.util.DebugUtil.debugLog;

public class Tickables
{
  protected List<Tickable> tickables;
  protected long tickCount;

  public Tickables()
  {
    this.tickables = new ArrayList<>();
    this.tickCount = 0;
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
      debugLog("---======--- Tick [" + tickCount + "]  Start settle iteration [" + count + "] ---======---");
      if (count == 200)
      {
        break;
      }

      settled = true;
      for (Tickable tickable : tickables)
      {
        List<TraceValue> oldTraceValues = tickable.getTraceValues();

        tickable.propagate();

        tickable.updateConnections();
        List<TraceValue> newTraceValues = tickable.getTraceValues();
        settled &= areSettled(oldTraceValues, newTraceValues);
      }

      count++;
    }
    while (!settled);

    debugLog("---======--- Tick [" + tickCount + "]  Settled in [" + count + "] iterations. ---======---");
    for (Tickable tickable : tickables)
    {
      tickable.donePropagation();

      debugLog(tickable.toDebugString());
    }
    tickCount++;
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

