package net.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC161
    extends IntegratedCircuit<LVC161Snapshot, LVC161Pins>
{
  public static final String TYPE = "4-bit Counter";

  protected long counterValue;
  protected long oldCounterValue;

  protected boolean clock;
  protected boolean fallingEdge;
  protected boolean risingEdge;

  public LVC161(String name, LVC161Pins pins)
  {
    super(name, pins);
    counterValue = 0;
    oldCounterValue = 0;
  }

  @Override
  public void tick()
  {
    PinValue masterResetB = getPins().getMRB();
    PinValue cetValue = getPins().getCET();
    boolean cet = cetValue.isHigh();

    if (masterResetB.isError() || masterResetB.isNotConnected() ||
        cetValue.isError() || cetValue.isNotConnected())
    {
      getPins().setOutputError();
      getPins().setCarryError();
    }
    else if (masterResetB.isUnknown() ||
             cetValue.isUnknown())
    {
      getPins().setOutputUnsettled();
      getPins().setCarryUnsettled();
    }
    else if (masterResetB.isLow())
    {
      counterValue = 0;
      oldCounterValue = 0;
      getPins().setOutput(0);
      getPins().setCarry(false);
    }
    else if (getPins().isParallelLoad())
    {
      BusValue input = getPins().getInput();
      if (input.isValid())
      {
        oldCounterValue = counterValue;

        counterValue = input.getValue();

        setOutput(cet);
      }
      else
      {
        getPins().setOutputError();
        getPins().setCarryError();
      }
    }
    else
    {
      PinValue countEnabled = getPins().getCEP();

      if (countEnabled.isError() || countEnabled.isNotConnected())
      {
        getPins().setOutputError();
        getPins().setCarryError();
      }
      else if (countEnabled.isUnknown())
      {
        getPins().setOutputUnsettled();
        getPins().setCarryUnsettled();
      }
      else if (countEnabled.isHigh())
      {
        boolean currentClock = getPins().isClock();

        fallingEdge = !currentClock && this.clock;
        risingEdge = currentClock && !this.clock;
        clock = currentClock;

        if (cet)
        {
          if (risingEdge)
          {
            oldCounterValue = counterValue;

            counterValue++;

            if (counterValue == 0x10)
            {
              counterValue = 0;
            }
          }
        }
        setOutput(cet);
      }
      else
      {
        setOutput(cet);
      }
    }
  }

  private void setOutput(boolean cet)
  {
    getPins().setCarry((oldCounterValue + 1 == 0x10) && cet);
    getPins().setOutput(counterValue);
  }

  @Override
  public LVC161Snapshot createSnapshot()
  {
    return new LVC161Snapshot(counterValue,
                              oldCounterValue,
                              clock,
                              fallingEdge,
                              risingEdge);
  }

  @Override
  public void restoreFromSnapshot(LVC161Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    clock = snapshot.clock;
    fallingEdge = snapshot.fallingEdge;
    risingEdge = snapshot.risingEdge;
    oldCounterValue = snapshot.oldCounterValue;
  }

  public String getCounterValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) counterValue));
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

