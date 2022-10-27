package net.logicim.domain.common;

import java.util.Set;

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
