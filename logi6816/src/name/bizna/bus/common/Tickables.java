package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;
import name.bizna.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.util.DebugUtil.debugLog;

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
    if (tickCount == 13)
    {
      int xxx = 0;
    }

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

    debugLog("---======--- Tick [" + tickCount + "] Settled in [" + count + "] iterations. ---======---");
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

