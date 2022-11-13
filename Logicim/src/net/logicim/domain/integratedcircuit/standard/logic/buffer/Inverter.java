package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.port.Port;

public class Inverter
    extends Buffer
{
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
    return "Inverter";
  }
}

