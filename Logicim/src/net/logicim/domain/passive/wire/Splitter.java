package net.logicim.domain.passive.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

import java.util.ArrayList;
import java.util.List;

public class Splitter
    extends Passive
{
  public List<TracePort> startPorts;
  public List<TracePort> endPorts;

  public Splitter(Circuit circuit, String name, int bitWidth)
  {
    super(circuit, name);

    startPorts = new ArrayList<>(bitWidth);
    for (int i = 0; i < bitWidth; i++)
    {
      startPorts.add(new TracePort("Start " + i, this));
    }

    endPorts = new ArrayList<>(bitWidth);
    for (int i = 0; i < bitWidth; i++)
    {
      endPorts.add(new TracePort("End " + i, this));
    }
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  @Override
  public String getType()
  {
    return "Splitter";
  }

  @Override
  public void disconnect(Simulation simulation)
  {
  }

  public TracePort getEndPort(int i)
  {
    return endPorts.get(i);
  }

  public List<TracePort> getStartPorts()
  {
    return startPorts;
  }
}

