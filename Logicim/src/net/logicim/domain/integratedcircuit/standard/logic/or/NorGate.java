package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class NorGate
    extends OrGate
{
  public static final String TYPE = "NOR Gate";

  public NorGate(SubcircuitSimulation containingSubcircuitSimulation,
                 String name,
                 OrGatePins pins)
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

