package name.bizna.bus.common;

import name.bizna.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trace
{
  protected List<Trace> connections;  //Only Trace connections, not Ports.
  protected TraceNet net;

  public Trace()
  {
    connections = new ArrayList<>();
    net = new TraceNet(this);
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

  private void setNet(TraceNet net)
  {
    this.net = net;
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

  public TraceValue updateNetValue(TraceValue value)
  {
    return net.update(value);
  }

  public TraceNet getNet()
  {
    return net;
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
}

