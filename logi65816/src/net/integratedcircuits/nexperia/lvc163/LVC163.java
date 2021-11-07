package net.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
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

    boolean previousReset = reset;
    reset = masterResetBValue.isLow();
    updateClock(clockValue.isHigh());

    if (previousReset)
    {
      if (clockRisingEdge)
      {
        reset();
      }
    }
    else
    {
      boolean carryInCountEnabled = carryInCountEnabledValue.isHigh();

      if (parallelLoadB.isLow())
      {
        BusValue input = getPins().getInput();
        parallelLoad(carryInCountEnabled,
                     input.getValue(),
                     input.isValid());
      }
      else
      {
        count(carryInCountEnabled, countEnabledValue.isHigh());
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
                              clockRisingEdge);
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

