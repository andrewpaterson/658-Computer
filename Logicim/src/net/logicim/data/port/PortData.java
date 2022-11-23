package net.logicim.data.port;

import net.logicim.data.common.EventData;
import net.logicim.data.port.event.PortOutputEventData;

import java.util.List;

public class PortData
{
  protected List<EventData> events;
  protected PortOutputEventData output;
  protected long traceId;

  public PortData(List<EventData> events, PortOutputEventData output, long traceId)
  {
    this.events = events;
    this.output = output;
    this.traceId = traceId;
  }
}

