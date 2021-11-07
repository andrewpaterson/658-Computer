package net.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.CounterCircuit;

public class LVC161
    extends CounterCircuit<LVC161Snapshot, LVC161Pins>
{
  public static final String TYPE = "4-bit Counter";

  public LVC161(String name, LVC161Pins pins)
  {
    super(name, pins);
    limit = 0x10;
  }

  @Override
  public void tick()
  {
    PinValue masterResetBValue = getPins().getMasterResetB();
    PinValue carryInCountEnabledValue = getPins().getCET();
    PinValue parallelLoadB = getPins().getParallelLoadB();
    PinValue clockValue = getPins().getClock();
    PinValue countEnabledValue = getPins().getCountEnabled();

    boolean previousReset = reset;
    reset = masterResetBValue.isLow();
    updateClock(clockValue.isHigh());

    if (previousReset)
    {
      reset();
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
  public LVC161Snapshot createSnapshot()
  {
    return new LVC161Snapshot(counterValue,
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

