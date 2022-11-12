package net.logicim.domain.common;

public abstract class Event
{
  protected long time;

  public Event(long time)
  {
    this.time = time;
  }

  public long getTime()
  {
    return time;
  }
}

