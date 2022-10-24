package net.logicim.domain.integratedcircuit.standard;

import net.logicim.domain.common.IntegratedCircuit;

public class NotGate extends IntegratedCircuit<NotGatePins>
{
  public NotGate(String name, NotGatePins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {

  }

  @Override
  public String getType()
  {
    return null;
  }
}

