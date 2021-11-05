package net.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC163
    extends IntegratedCircuit<LVC163Snapshot, LVC163Pins>
{
  public static final String TYPE = "4-bit Counter";

  protected long counterValue;
  protected long oldCounterValue;

  protected boolean reset;

  protected boolean clock;
  protected boolean fallingEdge;
  protected boolean risingEdge;

  public LVC163(String name, LVC163Pins pins)
  {
    super(name, pins);
    counterValue = 0;
    oldCounterValue = 0;
    reset = false;
  }

  @Override
  public void tick()
  {
    PinValue masterResetB = getPins().getMRB();
    PinValue cetValue = getPins().getCET();
    boolean cet = cetValue.isHigh();
    PinValue parallelLoadB = getPins().getPEB();

    boolean currentClock = getPins().isClock().isHigh();

    fallingEdge = !currentClock && this.clock;
    risingEdge = currentClock && !this.clock;
    clock = currentClock;

    if (reset)
    {
      if (risingEdge)
      {
        counterValue = 0;
        oldCounterValue = 0;
        getPins().setOutput(0);
        getPins().setCarry(false);
        reset = masterResetB.isLow();
      }
    }
    else
    {
      reset = masterResetB.isLow();
      if (parallelLoadB.isLow())
      {
        BusValue input = getPins().getInput();
        if (input.isValid())
        {
          oldCounterValue = counterValue;
          counterValue = input.getValue();

          setOutput(cet);
        }
        setOutput(cet);
      }
      else
      {
        PinValue countEnabled = getPins().getCEP();
        if (countEnabled.isHigh())
        {
          if (cet && risingEdge)
          {
            oldCounterValue = counterValue;
            counterValue++;
            if (counterValue == 0x10)
            {
              counterValue = 0;
            }
          }
          setOutput(cet);
        }
      }
    }
  }

  private void setOutput(boolean cet)
  {
    getPins().setCarry((oldCounterValue + 1 == 0x10) && cet);
    getPins().setOutput(counterValue);
  }

  @Override
  public LVC163Snapshot createSnapshot()
  {
    return new LVC163Snapshot(counterValue,
                              oldCounterValue,
                              reset,
                              clock,
                              fallingEdge,
                              risingEdge);
  }

  @Override
  public void restoreFromSnapshot(LVC163Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    clock = snapshot.clock;
    reset = snapshot.reset;
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

