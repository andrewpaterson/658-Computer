package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.common.Circuit;

public class XnorGate
    extends XorGate
{
  public static final String TYPE = "XNOR Gate";

  public XnorGate(Circuit circuit, String name, XorGatePins pins)
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

