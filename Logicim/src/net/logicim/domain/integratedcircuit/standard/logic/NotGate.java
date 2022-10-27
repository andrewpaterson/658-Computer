package net.logicim.domain.integratedcircuit.standard.logic;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.trace.TraceValue;

import java.util.List;

public class NotGate
    extends IntegratedCircuit<NotGatePins>
{
  public NotGate(String name, NotGatePins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick(long time, List<Port> updatedPorts)
  {
    Uniport input = pins.getInput();
    TraceValue inValue = input.readValue();
    if (inValue.isError() || inValue.isImpedance())
    {
      pins.getOutput().writeUnsettled();
    }
    else
    {
      boolean calculatedValue = !inValue.isHigh();
      pins.getOutput().writeBool(calculatedValue);
    }
  }

  @Override
  public String getType()
  {
    return "NOT Gate";
  }
}

