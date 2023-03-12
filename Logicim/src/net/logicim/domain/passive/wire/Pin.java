package net.logicim.domain.passive.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.TracePort;
import net.logicim.domain.passive.common.Passive;

import java.util.ArrayList;
import java.util.List;

public class Pin
    extends Passive
{
  protected List<TracePort> tracePorts;

  public Pin(Circuit circuit, String name, int portCount)
  {
    super(circuit, name);
    tracePorts = new ArrayList<>();
    for (int i = 0; i < portCount; i++)
    {
      tracePorts.add(new TracePort("Port " + i, this));
    }
  }

  @Override
  public String getType()
  {
    return "Pin";
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }
}

