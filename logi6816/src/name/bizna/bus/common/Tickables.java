package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;
import name.bizna.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.util.DebugUtil.debugLog;

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
    int afterSettleCount = 0;
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
        if (afterSettleCount > 0)
        {
          throw new EmulatorException("Settled unsettled.  Or something.");
        }
        afterSettleCount = 0;
        for (Tickable tickable : tickables)
        {
          tickable.undoPropagation();
        }
      }
      else
      {
        afterSettleCount++;
      }
      count++;
    }
    while (!settled || afterSettleCount < 3);

    debugLog("------ Settled in [" + count + "] iterations. ------");
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

