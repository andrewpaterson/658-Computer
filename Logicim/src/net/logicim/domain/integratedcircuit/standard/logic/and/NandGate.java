package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class NandGate
    extends AndGate
{
  public static final String TYPE = "NAND Gate";

  public NandGate(SubcircuitSimulation containingSubcircuitSimulation,
                  String name,
                  AndGatePins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

  protected AndLogic createLogic()
  {
    return new NandLogic();
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

