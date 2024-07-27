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

  public Splitter(Circuit circuit,
                  String name,
                  List<String> startPortNames,
                  List<String> endPortNames)
  {
    super(circuit, name);

    startPorts = new ArrayList<>(startPortNames.size());
    for (String portName : startPortNames)
    {
      startPorts.add(new TracePort(portName, this));
    }

    endPorts = new ArrayList<>(endPortNames.size());
    for (String portName : endPortNames)
    {
      endPorts.add(new TracePort(portName, this));
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

  public List<TracePort> getStartPorts()
  {
    return startPorts;
  }
}

