package net.logicim.domain.passive.power;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.PowerOutPort;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import static net.logicim.domain.passive.power.PowerPinNames.POWER;

public class PowerSource
    extends Passive
{
  protected PowerOutPort powerOutPort;

  public PowerSource(SubcircuitSimulation containingSubcircuitSimulation,
                     String name,
                     float voltage)
  {
    super(containingSubcircuitSimulation, name);
    powerOutPort = new PowerOutPort(POWER, this, voltage);
  }

  public PowerOutPort getPowerOutPort()
  {
    return powerOutPort;
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
  }

  public String getType()
  {
    return "Power Source";
  }
}

