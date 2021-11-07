package net.integratedcircuits.nexperia.lvc161;

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
    PinValue masterResetBValue = getPins().getMRB();
    PinValue carryInCountEnabledValue = getPins().getCET();
    PinValue parallelLoadB = getPins().getPEB();
    PinValue clockValue = getPins().getClock();
    PinValue countEnabledValue = getPins().getCEP();

    boolean previousReset = reset;
    reset = masterResetBValue.isLow();
    updateClock(clockValue);

    System.out.println("LVC161.tick");

    if (previousReset)
    {
      reset();
    }
    else
    {
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
  public LVC161Snapshot createSnapshot()
  {
    return new LVC161Snapshot(counterValue,
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

