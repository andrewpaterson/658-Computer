package net.logicim.data.common;

import net.logicim.domain.common.Event;

public abstract class EventData<E extends Event>
{
  public long time;
  public long id;

  public EventData(long time, long id)
  {
    this.time = time;
    this.id = id;
  }
}

