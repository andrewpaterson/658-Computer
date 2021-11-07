package net.integratedcircuits.ti.f251;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class F251
    extends IntegratedCircuit<F251Snapshot, F251Pins>
{
  public static final String TYPE = "1-of-8 Data Selector";

  public F251(String name, F251Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    PinValue enabledB = getPins().getGB();

    boolean outputError = false;
    boolean outputUnset = false;
    boolean outputHighImpedance = false;
    if (enabledB.isError() || enabledB.isNotConnected())
    {
      outputError = true;
    }
    else if (enabledB.isUnknown())
    {
      outputUnset = true;
    }
    else if (enabledB.isHigh())
    {
      outputHighImpedance = true;
    }

    BusValue selectorValue = getPins().getA();
    BusValue dataValue = getPins().getD();
    if (selectorValue.isUnknown() || selectorValue.isNotConnected() ||
        dataValue.isUnknown() || dataValue.isNotConnected())
    {
      outputUnset = true;
    }
    else if (selectorValue.isError())
    {
      outputError = true;
    }

    if (outputError)
    {
      getPins().setOutputError();
    }
    else if (outputUnset)
    {
      getPins().setOutputUnsettled();
    }
    else if (outputHighImpedance)
    {
      getPins().setOutputHighImpedance();
    }
    else
    {
      long selector = selectorValue.getValue();
      long data = dataValue.getValue();
      int x = 1 << selector;
      x = (int) data & x;
      boolean high = x != 0;

      getPins().setOutput(high, !high);
    }
  }

  @Override
  public F251Snapshot createSnapshot()
  {
    return new F251Snapshot();
  }

  @Override
  public void restoreFromSnapshot(F251Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

