package net.logicim.domain.common.trace;

import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.common.Event;
import net.logicim.domain.common.Port;

import static net.logicim.domain.common.trace.TraceNet.Unsettled;

public class Trace
{
  protected TraceNet net;
  protected LinkedList<Event> events;

  public Trace()
  {
    net = new TraceNet(this);
    events = new LinkedList<>();
  }

  public float getVoltage()
  {
    if (net != null)
    {
      return net.getVoltage();
    }
    else
    {
      return Unsettled;
    }
  }

  public void update(float voltage, Port port)
  {
    net.update(voltage, port);
  }

  public TraceNet getNet()
  {
    return net;
  }

  private void setNet(TraceNet net)
  {
    this.net = net;
  }

  public Port getDrivingPort()
  {
    if (net != null)
    {
      return net.getDrivingPort();
    }
    else
    {
      return null;
    }
  }

  public boolean isNotConnected()
  {
    return net == null;
  }

  public void add(Event event)
  {
    LinkedListIterator<Event> iterator = events.iterator();
    while (iterator.hasNext())
    {
      Event existingEvent = iterator.next();
      if (existingEvent.getTime() > event.getTime())
      {
        iterator.insertBefore(event);
        break;
      }
    }
  }
}

