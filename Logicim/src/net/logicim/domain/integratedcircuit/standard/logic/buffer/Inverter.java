package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Circuit;

public class Inverter
    extends Buffer
{
  public static final String TYPE = "Inverter";

  public Inverter(Circuit circuit, String name, BufferPins pins)
  {
    super(circuit, name, pins);
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

