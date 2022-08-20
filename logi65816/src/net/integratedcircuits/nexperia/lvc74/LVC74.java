package net.integratedcircuits.nexperia.lvc74;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC74
    extends IntegratedCircuit<LVC74Snapshot, LVC74Pins>
{
  public static final String TYPE = "D-type Flip Flop";

  protected boolean[] risingValue;
  protected boolean[] fallingValue;
  protected boolean[] clock;
  protected boolean[] clockRising;

  public LVC74(String name, LVC74Pins pins)
  {
    super(name, pins);
    risingValue = new boolean[2];
    fallingValue = new boolean[2];
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
    PinValue resetValue = getPins().getRDValue(flipFlop);
    PinValue dataValue = getPins().getDValue(flipFlop);
    PinValue clockValue = getPins().getCPValue(flipFlop);
    PinValue setValue = getPins().getSDValue(flipFlop);

    updateClock(flipFlop, clockValue.isHigh());

    if (resetValue.isLow())
    {
      risingValue[flipFlop] = false;
      fallingValue[flipFlop] = false;
    }
    else if (setValue.isLow())
    {
      risingValue[flipFlop] = true;
      fallingValue[flipFlop] = true;
    }
    else
    {
      fallingValue[flipFlop] = risingValue[flipFlop];
      if (clockRising[flipFlop])
      {
        risingValue[flipFlop] = dataValue.isHigh();
      }
    }

    getPins().setValue(flipFlop, fallingValue[flipFlop]);
  }

  public LVC74Snapshot createSnapshot()
  {
    return new LVC74Snapshot(risingValue[0],
                             risingValue[1],
                             fallingValue[0],
                             fallingValue[1],
                             clock[0],
                             clock[1],
                             clockRising[0],
                             clockRising[1]);
  }

  public void restoreFromSnapshot(LVC74Snapshot snapshot)
  {
    risingValue[0] = snapshot.risingValue1;
    risingValue[1] = snapshot.risingValue2;
    fallingValue[0] = snapshot.fallingValue1;
    fallingValue[1] = snapshot.fallingValue2;
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
    if (risingValue[flipFlop])
    {
      return "1";
    }
    else
    {
      return "0";
    }
  }
}

