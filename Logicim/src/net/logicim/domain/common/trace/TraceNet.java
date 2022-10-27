package net.logicim.domain.common.trace;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.common.TraceEvent;
import net.logicim.domain.common.port.Port;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TraceNet
{
  public static final float Unsettled = 0.5f;
  public static final float Undriven = -1.0f;

  protected LinkedList<TraceEvent> events;
  protected float voltage;

  protected Set<Port> connectedPorts;

  public TraceNet()
  {
    events = new LinkedList<>();
    voltage = Undriven;
    connectedPorts = new LinkedHashSet<>();
  }

  public void update(float value)
  {
    this.voltage = value;
  }

  public float getVoltage()
  {
    return voltage;
  }

  public List<Port> getInputPorts()
  {
    List<Port> inputPorts = new ArrayList<>();
    for (Port port : connectedPorts)
    {
      if (port.isInput())
      {
        inputPorts.add(port);
      }
    }
    return inputPorts;
  }

  public void add(TraceEvent event)
  {
    LinkedListIterator<TraceEvent> iterator = events.iterator();
    boolean added = false;
    while (iterator.hasNext())
    {
      TraceEvent existingEvent = iterator.next();
      if (existingEvent.getTime() > event.getTime())
      {
        added = true;
        iterator.insertBefore(event);
        break;
      }
    }
    if (!added)
    {
      events.add(event);
    }
  }

  public boolean isUndriven()
  {
    return voltage == Undriven;
  }

  public void initialise()
  {
    update(Unsettled);
  }

  public void remove(TraceEvent event)
  {
    boolean removed = events.remove(event);
    if (!removed)
    {
      throw new SimulatorException("Cannot remove event");
    }
  }

  public void connect(Port port)
  {
    connectedPorts.add(port);
  }
}

