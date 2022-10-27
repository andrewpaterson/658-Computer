package net.logicim.domain.common;

import static net.logicim.domain.common.Units.nS_IN_S;

public abstract class LongTime
{
  public static int timeToNanoseconds(long time)
  {
    return (int)(time / 1024);
  }

  public static int nanosecondsToTime(int nanoseconds)
  {
    return nanoseconds * 1024;
  }

  public static int nanosecondsToTime(float nanoseconds)
  {
    return (int) (nanoseconds * 1024);
  }

  public static long secondsToTime(float seconds)
  {
    return nanosecondsToTime((long)((double) nS_IN_S * seconds));
  }

  public float timeToSeconds(long time)
  {
    return (float) ((double) timeToNanoseconds(time) / (double) nS_IN_S);
  }
}

