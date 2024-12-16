package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class XnorGate
    extends XorGate
{
  public static final String TYPE = "XNOR Gate";

  public XnorGate(SubcircuitSimulation containingSubcircuitSimulation,
                  String name,
                  XorGatePins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
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

