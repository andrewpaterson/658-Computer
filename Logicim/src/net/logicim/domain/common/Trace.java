package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trace
{
  protected List<Trace> connections;  //Only connected Traces, not connected Ports.
  protected TraceNet net;

  public Trace()
  {
    connections = new ArrayList<>();
    net = new TraceNet(this);
  }

  private static void recurse(Set<Trace> allConnections, List<Trace> stack)
  {
    while (!stack.isEmpty())
    {
      Trace trace = stack.get(stack.size() - 1);
      stack.remove(stack.size() - 1);

      for (Trace connection : trace.connections)
      {
        if (!allConnections.contains(connection))
        {
          allConnections.add(connection);
          stack.add(connection);
        }
      }
    }
  }

  public void connect(Trace trace)
  {
    connections.add(trace);
    trace.connections.add(this);

    List<Trace> connected = findConnected();
    TraceNet net = new TraceNet(connected);
    for (Trace connection : connected)
    {
      connection.setNet(net);
    }
  }

  public List<Trace> findConnected()
  {
    Set<Trace> allConnections = new HashSet<>();
    List<Trace> stack = new ArrayList<>();
    stack.add(this);
    allConnections.add(this);

    recurse(allConnections, stack);
    return new ArrayList<>(allConnections);
  }

  public TraceValue getValue()
  {
    if (net != null)
    {
      return net.getValue();
    }
    else
    {
      return TraceValue.Unsettled;
    }
  }

  public TraceValue updateNetValue(TraceValue value, Port port)
  {
    return net.update(value, port);
  }

  public TraceNet getNet()
  {
    return net;
  }

  private void setNet(TraceNet net)
  {
    this.net = net;
  }

  @Override
  public String toString()
  {
    TraceValue value = getNet().getValue();
    return StringUtil.toEnumString(value);
  }

  public char getStringValue()
  {
    return net.getValue().getStringValue();
  }

  public Port get_DEBUG_lastPortThatUpdated()
  {
    if (net != null)
    {
      return net.get_DEBUG_lastPortThatUpdated();
    }
    else
    {
      return null;
    }
  }

  public boolean isHigh()
  {
    return net != null && net.getValue().isHigh();
  }

  public boolean isError()
  {
    return net != null && net.getValue().isError();
  }

  public boolean isLow()
  {
    return net != null && net.getValue().isLow();
  }

  public boolean isUnsettled()
  {
    return net != null && net.getValue().isUnsettled();
  }

  public boolean isNotConnected()
  {
    return net == null;
  }
}

