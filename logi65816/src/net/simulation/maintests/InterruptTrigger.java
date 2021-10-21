package net.simulation.maintests;

public class InterruptTrigger
{
  private final String testString;
  private final int delay;
  private int count;

  public InterruptTrigger(String testString, int delay)
  {
    this.testString = testString;
    this.delay = delay;
    this.count = 0;
  }

  boolean test(String s)
  {
    if (count == 0 && testString.equals(s))
    {
      count = 1;
      return true;
    }
    else if (count >= 1 && count < delay)
    {
      count++;
      return true;
    }
    else if (count >= delay && count < delay + 4)
    {
      count++;
      return false;
    }
    else
    {
      return true;
    }
  }
}

