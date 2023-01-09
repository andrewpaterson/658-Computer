package net.logicim.domain.passive.wiring;

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
  public List<Port> startPorts;
  public List<Port> endPorts;

  public Splitter(Circuit circuit, String name, int endCount)
  {
    super(circuit, name);

    startPorts = new ArrayList<>(endCount);
    for (int i = 0; i < endCount; i++)
    {
      startPorts.add(new TracePort("Start " + i, this));
    }
    ports.addAll(startPorts);

    endPorts = new ArrayList<>(endCount);
    for (int i = 0; i < endCount; i++)
    {
      endPorts.add(new TracePort("End " + i, this));
    }
    ports.addAll(endPorts);
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

  public Port getEndPort(int i)
  {
    return endPorts.get(i);
  }

  public List<Port> getStartPorts()
  {
    return startPorts;
  }
}

