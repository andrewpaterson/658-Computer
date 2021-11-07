package net.integratedcircuits.nexperia.lvc163;

import net.common.PinValue;
import net.integratedcircuits.common.counter.CounterCircuit;

public class LVC163
    extends CounterCircuit<LVC163Snapshot, LVC163Pins>
{
  public static final String TYPE = "4-bit Counter";

  public LVC163(String name, LVC163Pins pins)
  {
    super(name, pins);
    limit = 0x10;
  }

  @Override
  public void tick()
  {
    PinValue masterResetBValue = getPins().getMRB();
    PinValue carryInCountEnabledValue = getPins().getCET();
    PinValue parallelLoadB = getPins().getPEB();
    PinValue clockValue = getPins().getClock();
    PinValue countEnabledValue = getPins().getCEP();

    updateClock(clockValue);

    if (reset)
    {
      if (risingEdge)
      {
        reset();
      }
    }
    else
    {
      reset = masterResetBValue.isLow();
      boolean carryInCountEnabled = carryInCountEnabledValue.isHigh();
      boolean countEnabled = countEnabledValue.isHigh();

      if (parallelLoadB.isLow())
      {
        parallelLoad(carryInCountEnabled);
      }
      else
      {
        count(carryInCountEnabled, countEnabled);
      }
    }
  }

  @Override
  public LVC163Snapshot createSnapshot()
  {
    return new LVC163Snapshot(counterValue,
                              oldCounterValue,
                              reset,
                              clock,
                              fallingEdge,
                              risingEdge);
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

