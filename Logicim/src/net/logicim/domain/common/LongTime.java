package net.logicim.domain.common;

import net.logicim.ui.SimulatorPanel;

public abstract class LongTime
{
  public static int timeToNanoseconds(long time)
  {
    return (int)(time / 1024);
  }

  public static long nanosecondsToTime(int nanoseconds)
  {
    return nanoseconds * 1024;
  }

  public float timeToSeconds(long time)
  {
    return (float) ((double) timeToNanoseconds(time) / (double)SimulatorPanel.NANOS_IN_UNIT);
  }
}

