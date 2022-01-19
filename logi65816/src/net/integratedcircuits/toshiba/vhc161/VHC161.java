package net.integratedcircuits.toshiba.vhc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.UpCounterCircuit;

public class VHC161
    extends UpCounterCircuit<VHC161Snapshot, VHC161Pins>
{
  public static final String TYPE = "4-bit Up Counter";

  public VHC161(String name, VHC161Pins pins)
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
  public VHC161Snapshot createSnapshot()
  {
    return new VHC161Snapshot(counterValue,
                              oldCounterValue,
                              reset,
                              clock,
                              clockRising);
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

