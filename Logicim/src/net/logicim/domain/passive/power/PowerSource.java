package net.logicim.domain.passive.power;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.passive.common.Passive;

public class PowerSource
    extends Passive
{
  protected PowerOutPort powerOutPort;
  public PowerSource(Circuit circuit, String name, float voltage)
  {
    super(circuit, name);
    powerOutPort = new PowerOutPort("Power", this, voltage);
  }

  public PowerOutPort getPowerOutPort()
  {
    return powerOutPort;
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

