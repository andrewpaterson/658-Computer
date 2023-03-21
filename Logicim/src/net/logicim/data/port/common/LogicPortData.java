package net.logicim.data.port.common;

import net.logicim.data.port.event.PortEventData;
import net.logicim.data.port.event.PortOutputEventData;

import java.util.List;

public class LogicPortData
    extends PortData
{
  public List<PortEventData<?>> events;
  public PortOutputEventData<?> output;

  public LogicPortData()
  {
  }

  public LogicPortData(List<PortEventData<?>> events, PortOutputEventData<?> output, long traceId)
  {
    super(traceId);
    this.events = events;
    this.output = output;
  }
}

