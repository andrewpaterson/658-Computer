package net.integratedcircuits.ti.hct40103;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class HCT40103
    extends IntegratedCircuit<HCT40103Snapshot, HCT40103Pins>
{
  public static final String TYPE = "8-bit Down Counter";

  protected long counterValue;
  protected long syncLoadValue;
  protected boolean clock;

  public HCT40103(String name, HCT40103Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    boolean clockRisingEdge;

    PinValue masterResetValue = getPins().getMRB();
    PinValue parallelLoadAsyncBValue = getPins().getPLB();
    PinValue parallelLoadSyncBValue = getPins().getPEB();
    PinValue countInhibitBValue = getPins().getTEB();

    boolean reset = masterResetValue.isLow();
    boolean parallelLoadAsync = parallelLoadAsyncBValue.isLow();
    boolean parallelLoadSync = parallelLoadSyncBValue.isLow();
    boolean countInhibit = countInhibitBValue.isHigh();

    boolean upClock = getPins().getCP().isHigh();
    clockRisingEdge = upClock && !this.clock;
    this.clock = upClock;

    if (reset)
    {
      reset();
      getPins().setOutput(counterValue);
    }
    else if (parallelLoadAsync)
    {
      BusValue input = getPins().getInput();
      counterValue = input.getValue() & 0xff;
      syncLoadValue = -1;
    }
    else if (parallelLoadSync)
    {
      BusValue input = getPins().getInput();
      syncLoadValue = input.getValue() & 0xff;
    }
    else
    {
      if (!countInhibit)
      {
        if (clockRisingEdge)
        {
          if (syncLoadValue == -1)
          {
            counterValue--;
            if (counterValue == -1)
            {
              counterValue = 255;
            }
          }
          else
          {
            counterValue = syncLoadValue;
            syncLoadValue = -1;
          }
        }
        getPins().setOutput(counterValue);
      }
    }

    if (!countInhibit)
    {
      getPins().setTCB(counterValue != 0);
    }
    else
    {
      getPins().setTCB(true);
    }
  }

  protected void reset()
  {
    counterValue = 255;
    syncLoadValue = -1;
    getPins().setOutput(0);
    getPins().setTCB(true);
  }

  @Override
  public HCT40103Snapshot createSnapshot()
  {
    return new HCT40103Snapshot(counterValue,
                                syncLoadValue,
                                clock);
  }

  @Override
  public void restoreFromSnapshot(HCT40103Snapshot snapshot)
  {
    counterValue = snapshot.counterValue;
    syncLoadValue = snapshot.syncLoadValue;
    clock = snapshot.clock;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getCounterValueString()
  {
    return StringUtil.getByteStringHex(toByte((int) counterValue));
  }
}

