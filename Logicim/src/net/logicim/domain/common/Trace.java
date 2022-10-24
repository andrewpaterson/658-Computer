package net.logicim.domain.common;

import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.common.collection.linkedlist.LinkedListNode;

import static net.logicim.domain.common.TraceNet.Unsettled;

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

  public Port get_DEBUG_lastPortThatUpdated()
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

