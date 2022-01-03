package net.integratedcircuits.nexperia.lvc273;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC273
    extends IntegratedCircuit<LVC273Snapshot, LVC273Pins>
{
  public static final String TYPE = "8-bit Latch";

  protected long latchValue;
  protected boolean clock;

  public LVC273(String name, LVC273Pins pins)
  {
    super(name, pins);
    latchValue = 0;
    clock = false;
  }

  @Override
  public void tick()
  {
    PinValue clockPulseValue = getPins().getCP();
    PinValue masterResetB = getPins().getMRB();

    boolean clock = clockPulseValue.isHigh();
    boolean clockRisingEdge = clock && !this.clock;
    this.clock = clock;

    boolean outputUnset = false;
    boolean outputError = false;

    if (clockRisingEdge)
    {
      BusValue input = getPins().getInput();
      if (input.isUnknown() || input.isNotConnected())
      {
        outputUnset = true;
      }
      else if (input.isError())
      {
        outputError = true;
      }
      else
      {
        latchValue = input.getValue();
      }
    }

    if (masterResetB.isLow())
    {
      latchValue = 0;
    }

    if (outputError)
    {
      getPins().setOutputError();
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled();
    }
    else
    {
      getPins().setOutput(latchValue);
    }
  }

  @Override
  public LVC273Snapshot createSnapshot()
  {
    return new LVC273Snapshot(latchValue, clock);
  }

  @Override
  public void restoreFromSnapshot(LVC273Snapshot snapshot)
  {
    latchValue = snapshot.latchValue;
    clock = snapshot.clock;
  }

  public String getValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) latchValue));
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

