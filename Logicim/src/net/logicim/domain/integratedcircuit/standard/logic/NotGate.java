package net.logicim.domain.integratedcircuit.standard.logic;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Uniport;
import net.logicim.domain.common.trace.TraceValue;

public class NotGate
    extends IntegratedCircuit<NotGatePins>
{
  public NotGate(String name, NotGatePins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    Uniport input = pins.getInput();
    TraceValue inValue = input.readValue();
    if (inValue.isError() || inValue.isNotConnected())
    {
      pins.getOutput().writeUnsettled();
    }
    else
    {
      boolean calculatedValue = !inValue.isHigh();
      getPins().getOutput().writeBool(calculatedValue);
    }
  }

  @Override
  public String getType()
  {
    return "NOT Gate";
  }
}

