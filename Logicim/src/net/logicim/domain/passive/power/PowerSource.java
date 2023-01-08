package net.logicim.domain.passive.power;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PortType;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.passive.common.Passive;

public class PowerSource
    extends Passive
{
  public PowerSource(Circuit circuit, String name, float voltage)
  {
    super(circuit, name);
    ports.add(new PowerOutPort(PortType.PowerOut, "Power", this, voltage));
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  public float getVoltageOut(long time)
  {
    return getPowerSourcePort().getVoltageOut(time);
  }

  protected PowerOutPort getPowerSourcePort()
  {
    return (PowerOutPort) ports.get(0);
  }

  public String getType()
  {
    return "Power Source";
  }
}

