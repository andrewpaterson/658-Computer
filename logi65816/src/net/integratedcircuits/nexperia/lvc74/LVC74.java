package net.integratedcircuits.nexperia.lvc74;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC74
    extends IntegratedCircuit<LVC74Snapshot, LVC74Pins>
{
  public static final String TYPE = "D-type Flip Flop";

  protected boolean[] value;
  protected boolean[] clock;
  protected boolean[] clockRising;

  public LVC74(String name, LVC74Pins pins)
  {
    super(name, pins);
    value = new boolean[2];
    clock = new boolean[2];
    clockRising = new boolean[2];
  }

  public void tick()
  {
    tickFlipFlop(LVC74Pins.FLIP_FLOP_1_INDEX);
    tickFlipFlop(LVC74Pins.FLIP_FLOP_2_INDEX);
  }

  protected void updateClock(int flipFlop, boolean clock)
  {
    this.clockRising[flipFlop] = clock && !this.clock[flipFlop];
    this.clock[flipFlop] = clock;
  }

  private void tickFlipFlop(int flipFlop)
  {
    PinValue rdValue = getPins().getRDValue(flipFlop);
    PinValue dValue = getPins().getPrevDValue(flipFlop);
    PinValue cpValue = getPins().getCPValue(flipFlop);
    PinValue sdValue = getPins().getSDValue(flipFlop);

    updateClock(flipFlop, cpValue.isHigh());

    if (rdValue.isLow())
    {
      value[flipFlop] = false;
    }
    else if (sdValue.isLow())
    {
      value[flipFlop] = true;
    }
    else
    {
      if (clockRising[flipFlop])
      {
        value[flipFlop] = dValue.isHigh();
      }
    }

    getPins().setValue(flipFlop, value[flipFlop]);
  }

  public LVC74Snapshot createSnapshot()
  {
    return new LVC74Snapshot(value[0], value[1], clock[0], clock[1], clockRising[0], clockRising[1]);
  }

  public void restoreFromSnapshot(LVC74Snapshot snapshot)
  {
    value[0] = snapshot.value1;
    value[1] = snapshot.value2;
    clock[0] = snapshot.clock1;
    clock[1] = snapshot.clock2;
    clockRising[0] = snapshot.clockRising1;
    clockRising[1] = snapshot.clockRising2;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getValueString(int flipFlop)
  {
    if (value[flipFlop])
    {
      return "1";
    }
    else
    {
      return "0";
    }
  }
}

