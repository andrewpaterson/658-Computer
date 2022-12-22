package net.logicim.domain.power;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortHolder;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerOutPort;

import java.util.ArrayList;
import java.util.List;

public class PowerSource
    extends PortHolder
{
  protected Circuit circuit;
  protected String name;
  protected boolean enabled;

  public PowerSource(Circuit circuit, String name, float voltage)
  {
    ports = new ArrayList<>();
    ports.add(new PowerOutPort(PortType.PowerOut, "Power", this, voltage));

    this.circuit = circuit;
    this.name = name;
    this.enabled = true;
  }

  public List<Port> getPorts()
  {
    return ports;
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  @Override
  public String getDescription()
  {
    return null;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public float getVoltageOut()
  {
    return getPowerSourcePort().getVoltageOut();
  }

  protected PowerOutPort getPowerSourcePort()
  {
    return (PowerOutPort) ports.get(0);
  }

  public String getType()
  {
    return "Power Source";
  }

  public Port getPort(String name)
  {
    for (Port port : ports)
    {
      if (port.getName().equals(name))
      {
        return port;
      }
    }
    return null;
  }

  public void disable()
  {
    enabled = false;
  }
}

