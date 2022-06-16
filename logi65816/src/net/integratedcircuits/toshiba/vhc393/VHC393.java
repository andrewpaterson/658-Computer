package net.integratedcircuits.toshiba.vhc393;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toNybble;

public class VHC393
    extends IntegratedCircuit<VHC393Snapshot, VHC393Pins>
{
  public static final String TYPE = "4-bit Up Counter";

  private int limit;
  private boolean reset[];
  private boolean clock[];
  private long counterValue[];

  public VHC393(String name, VHC393Pins pins)
  {
    super(name, pins);
    limit = 0x10;
    reset = new boolean[2];
    clock = new boolean[2];
    counterValue = new long[2];
  }

  @Override
  public void tick()
  {
    tick(0);
    tick(1);
  }

  private void tick(int index)
  {
    PinValue masterResetBValue = getPins().getClear(index);
    PinValue clockValue = getPins().getClockB(index);

    boolean previousReset = reset[index];
    reset[index] = masterResetBValue.isHigh();
    boolean clockFalling = updateClock(index, clockValue.isHigh());

    if (previousReset)
    {
      reset(index);
    }
    else
    {
      count(index, clockFalling);
    }
  }

  protected void reset(int index)
  {
    counterValue[index] = 0;
    getPins().setOutput(index, 0);
  }

  protected void count(int index, boolean clockRising)
  {
    if (clockRising)
    {
      counterValue[index]++;
      if (counterValue[index] == getLimit())
      {
        counterValue[index] = 0;
      }
    }

    getPins().setOutput(index, counterValue[index]);
  }

  private int getLimit()
  {
    return limit;
  }

  protected boolean updateClock(int index, boolean clock)
  {
    boolean clockFalling = !clock && this.clock[index];
    this.clock[index] = clock;
    return clockFalling;
  }

  @Override
  public VHC393Snapshot createSnapshot()
  {
    return new VHC393Snapshot(counterValue[0], reset[0], clock[0], counterValue[1], reset[1], clock[1]);
  }

  @Override
  public void restoreFromSnapshot(VHC393Snapshot snapshot)
  {
    counterValue[0] = snapshot.counterValue1;
    clock[0] = snapshot.clock1;
    reset[0] = snapshot.reset1;
    counterValue[1] = snapshot.counterValue2;
    clock[1] = snapshot.clock2;
    reset[1] = snapshot.reset2;
  }

  public String getCounterValueString(int index)
  {
    return StringUtil.getNybbleStringHex(toNybble((int) counterValue[index]));
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

