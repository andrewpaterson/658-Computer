package net.integratedcircuits.wdc.wdc65816;

public class TimeRange
{
  public int start;
  public int stop;  //Inclusive

  public TimeRange()
  {
    this.start = -1;
    this.stop = -1;
  }

  public void set(int start, int stop)
  {
    this.start = start;
    this.stop = stop;
  }

  public boolean timeIn(int time)
  {
    return timeIn(time, start, stop);
  }

  public static boolean timeIn(int time, int start, int stop)
  {
    if (start == stop)
    {
      return time == start;
    }
    else if (start < stop)
    {
      return time >= start && time <= stop;
    }
    else
    {
      return time <= stop || time >= start;
    }
  }
}

