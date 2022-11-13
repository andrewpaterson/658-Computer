package net.logicim.domain.common;

import static net.logicim.domain.common.Units.nS_IN_S;

public abstract class LongTime
{
  public static final int timeGranularityInNanosecond = 1024;

  public static int timeToNanoseconds(long time)
  {
    return (int)(time / timeGranularityInNanosecond);
  }

  public static int nanosecondsToTime(int nanoseconds)
  {
    return nanoseconds * timeGranularityInNanosecond;
  }

  public static int nanosecondsToTime(float nanoseconds)
  {
    return (int) (nanoseconds * timeGranularityInNanosecond);
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

