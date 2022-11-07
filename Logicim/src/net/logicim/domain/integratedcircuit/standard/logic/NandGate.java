package net.logicim.domain.integratedcircuit.standard.logic;

import net.logicim.domain.common.Circuit;

public class NandGate
    extends AndGate
{
  public NandGate(Circuit circuit, String name, AndGatePins pins)
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
    return "NAND Gate";
  }
}

