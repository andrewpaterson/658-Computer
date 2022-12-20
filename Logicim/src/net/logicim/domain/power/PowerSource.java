package net.logicim.domain.power;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.BasePort;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerSourcePort;

import java.util.ArrayList;
import java.util.List;

public class PowerSource
{
  protected List<BasePort> ports;
  protected Circuit circuit;
  protected String name;

  public PowerSource(Circuit circuit, String name, float voltage)
  {
    ports = new ArrayList<>();
    ports.add(new PowerSourcePort(PortType.PowerOut, "Power", voltage));

    this.circuit = circuit;
    this.name = name;
  }

  public List<BasePort> getPorts()
  {
    return ports;
  }

  public float getVoltageOut()
  {
    return getPowerSourcePort().getVoltageOut();
  }

  protected PowerSourcePort getPowerSourcePort()
  {
    return (PowerSourcePort)ports.get(0);
  }

  public String getType()
  {
    return "Power Source";
  }

  public BasePort getPort(String name)
  {
    for (BasePort port : ports)
    {
      if (port.getName().equals(name))
      {
        return port;
      }
    }
    return null;
  }
}

