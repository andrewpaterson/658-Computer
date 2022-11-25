package net.logicim.data.common.event;

import net.logicim.data.SaveData;
import net.logicim.domain.common.event.Event;

public abstract class EventData<E extends Event>
    extends SaveData
{
  public long time;
  public long id;

  public EventData(long time, long id)
  {
    this.time = time;
    this.id = id;
  }
}

