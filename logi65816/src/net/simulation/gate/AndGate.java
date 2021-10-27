package net.simulation.gate;

import net.common.Snapshot;
import net.simulation.common.Uniport;

import java.util.List;

import static net.common.PinValue.High;

public class AndGate
    extends LogicGate<Snapshot, AndGateTickablePins>
{
  protected List<Uniport> in;
  protected Uniport out;

  public AndGate(String name, AndGateTickablePins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    andOrLogic(High);
  }

  @Override
  public String getType()
  {
    return "AND Gate";
  }
}

