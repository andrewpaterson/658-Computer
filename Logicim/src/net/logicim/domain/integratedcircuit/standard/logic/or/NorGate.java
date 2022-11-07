package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.common.Circuit;

public class NorGate
    extends OrGate
{
  public NorGate(Circuit circuit, String name, OrGatePins pins)
  {
    super(circuit, name, pins);
  }

  @Override
  protected boolean transformOutput(boolean value)
  {
    return !value;
  }

  @Override
  public String getType()
  {
    return "NOR Gate";
  }
}

