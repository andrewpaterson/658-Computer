package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.common.Circuit;

public class NandGate
    extends AndGate
{
  public static final String TYPE = "NAND Gate";

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
    return TYPE;
  }
}

