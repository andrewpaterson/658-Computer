package net.logicim.domain.passive.wiring;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

public class Splitter
    extends Passive
{
  public Splitter(Circuit circuit, String name, int endCount)
  {
    super(circuit, name);
    for (int i = 0; i < endCount; i++)
    {
      ports.add(new TracePort("End " + i, this));
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

  public Port getEndPort(int i)
  {
    return null;
  }
}

