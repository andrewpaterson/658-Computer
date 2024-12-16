package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

public class Inverter
    extends Buffer
{
  public static final String TYPE = "Inverter";

  public Inverter(SubcircuitSimulation containingSubcircuitSimulation,
                  String name,
                  BufferPins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

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

