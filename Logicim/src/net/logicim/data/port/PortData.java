package net.logicim.data.port;

import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;

import java.util.List;

public class PortData
{
  public List<PortEventData<?>> events;
  public PortOutputEventData<?> output;
  public long traceId;

  public PortData(List<PortEventData<?>> events, PortOutputEventData<?> output, long traceId)
  {
    this.events = events;
    this.output = output;
    this.traceId = traceId;
  }
}

