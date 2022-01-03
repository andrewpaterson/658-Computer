package net.integratedcircuits.nexperia.hc4040;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

public class HC4040
    extends IntegratedCircuit<HC4040Snapshot, HC4040Pins>
{
  public static final String TYPE = "12-bit Up Counter";

  protected int limit;
  protected long counterValue;
  protected long oldCounterValue;
  protected boolean reset;
  protected boolean clock;
  protected boolean clockFallingEdge;

  public HC4040(String name, HC4040Pins pins)
  {
    super(name, pins);
    limit = 0x2000;
  }

  @Override
  public void tick()
  {
    PinValue masterResetBValue = getPins().getMasterResetB();
    PinValue clockValue = getPins().getClock();

    boolean previousReset = reset;
    reset = masterResetBValue.isHigh();
    updateClock(clockValue.isHigh());

    if (previousReset)
    {
      counterValue = 0;
      oldCounterValue = 0;
    }
    else
    {
      if (clockFallingEdge)
      {
        oldCounterValue = counterValue;
        counterValue++;
        if (counterValue == limit)
        {
          counterValue = 0;
        }
      }
    }

    getPins().setOutput(counterValue & 0x1fff);
  }

  protected void updateClock(boolean currentClock)
  {
    this.clockFallingEdge = !currentClock && this.clock;
    this.clock = currentClock;
  }

  @Override
  public HC4040Snapshot createSnapshot()
  {
    return new HC4040Snapshot(counterValue,
                              oldCounterValue,
                              reset,
                              clock,
                              clockFallingEdge);
  }

  @Override
  public void restoreFromSnapshot(HC4040Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    oldCounterValue = snapshot.oldCounterValue;
    reset = snapshot.reset;
    clock = snapshot.clock;
    clockFallingEdge = snapshot.clockFallingEdge;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getCounterValueString()
  {
    return StringUtil.getWordStringHex((int) (counterValue & 0x1fff));
  }
}

