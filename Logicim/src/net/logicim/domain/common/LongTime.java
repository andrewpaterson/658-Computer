package net.logicim.domain.common;

import static net.logicim.domain.common.Units.nS_IN_S;

public abstract class LongTime
{
  public static final long timeGranularityInNanosecond = 1024;

  public static long timeToNanoseconds(long time)
  {
    return (time / timeGranularityInNanosecond);
  }

  public static long nanosecondsToTime(long nanoseconds)
  {
    return nanoseconds * timeGranularityInNanosecond;
  }

  public static long nanosecondsToTime(double nanoseconds)
  {
    return (long) (nanoseconds * timeGranularityInNanosecond);
  }

  public static long secondsToTime(double seconds)
  {
    return nanosecondsToTime(((double) nS_IN_S * seconds));
  }

  public double timeToSeconds(long time)
  {
    return timeToNanoseconds(time) / (double) nS_IN_S;
  }

  public static long frequencyToTime(double frequency)
  {
    return secondsToTime((1.0f / frequency));
  }
}

