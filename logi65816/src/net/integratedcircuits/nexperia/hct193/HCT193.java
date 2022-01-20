package net.integratedcircuits.nexperia.hct193;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toNybble;

public class HCT193
    extends IntegratedCircuit<HCT193Snapshot, HCT193Pins>
{
  public static final String TYPE = "4-bit Up/Down Counter";

  protected int upperLimit;
  protected int lowerLimit;
  protected long counterValue;
  protected boolean upClock;
  protected boolean downClock;

  public HCT193(String name, HCT193Pins pins)
  {
    super(name, pins);
    upperLimit = 0x10;
    lowerLimit = -1;
  }

  @Override
  public void tick()
  {
    boolean upClockRisingEdge;
    boolean upClockFallingEdge;
    boolean downClockRisingEdge;
    boolean downClockFallingEdge;

    PinValue masterResetValue = getPins().getMR();
    PinValue parallelLoadB = getPins().getPLB();

    boolean reset = masterResetValue.isHigh();

    boolean upClock = getPins().getCPU().isHigh();
    upClockRisingEdge = upClock && !this.upClock;
    upClockFallingEdge = !upClock && this.upClock;
    this.upClock = upClock;

    boolean downClock = getPins().getCPD().isHigh();
    downClockRisingEdge = downClock && !this.downClock;
    downClockFallingEdge = !downClock && this.downClock;
    this.downClock = downClock;

    if (reset)
    {
      reset();
    }
    else
    {
      boolean carry = false;
      boolean borrow = false;
      if (!parallelLoadB.isHigh())
      {
        BusValue input = getPins().getInput();
        if (input.isValid())
        {
          this.counterValue = input.getValue();
        }
      }
      else
      {
        if (this.downClock)
        {
          if (upClockRisingEdge)
          {
            counterValue++;
            if (counterValue == upperLimit)
            {
              counterValue = lowerLimit + 1;
            }
          }

          if (upClockFallingEdge)
          {
            if (counterValue == upperLimit - 1)
            {
              carry = true;
            }
          }
        }

        if (this.upClock)
        {
          if (downClockRisingEdge)
          {
            counterValue--;
            if (counterValue == lowerLimit)
            {
              counterValue = upperLimit - 1;
            }
          }

          if (downClockFallingEdge)
          {
            if (counterValue == lowerLimit + 1)
            {
              borrow = true;
            }
          }
        }
      }

      getPins().setOutput(counterValue);
      getPins().setTCUB(!carry);
      getPins().setTCDB(!borrow);
    }
  }

  protected void reset()
  {
    counterValue = 0;
    getPins().setOutput(0);
    getPins().setTCUB(true);
    getPins().setTCDB(true);
  }

  @Override
  public HCT193Snapshot createSnapshot()
  {
    return new HCT193Snapshot(counterValue,
                              upClock,
                              downClock);
  }

  @Override
  public void restoreFromSnapshot(HCT193Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    upClock = snapshot.upClock;
    downClock = snapshot.downClock;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getCounterValueString()
  {
    return StringUtil.getNybbleStringHex(toNybble((int) counterValue));
  }
}

