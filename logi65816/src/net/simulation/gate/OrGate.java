package net.simulation.gate;

import net.common.Snapshot;

import static net.common.PinValue.Low;

public class OrGate
    extends LogicGate<Snapshot, OrGateTickablePins>
{
  public OrGate(String name, OrGateTickablePins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    andOrLogic(Low);
  }

  @Override
  public String getType()
  {
    return "OR Gate";
  }
}

