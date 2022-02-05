package net.integratedcircuits.diodesinc.pib3b3251;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class B3251
    extends IntegratedCircuit<B3251Snapshot, B3251Pins>
{
  public static final String TYPE = "8-to-1 Multiplexer";

  public B3251(String name, B3251Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    BusValue selectValue = getPins().getSelect();
    BusValue inputValue = getPins().getInput();
    PinValue eb = getPins().getEB();

    if (eb.isLow())
    {
      if (inputValue.isValid() && selectValue.isValid())
      {
        long input = inputValue.getValue();
        long select = selectValue.getValue();
        input = (input >> select) & 1;
        getPins().setY(input == 1);
      }
      else
      {
        getPins().setYUnsettled();
      }
    }
    else if (eb.isHigh())
    {
      getPins().setYHighImpedance();
    }
    else if (eb.isNotConnected() || eb.isUnknown())
    {
      getPins().setYUnsettled();
    }
    else if (eb.isError())
    {
      getPins().setYError();
    }
  }

  @Override
  public B3251Snapshot createSnapshot()
  {
    return new B3251Snapshot();
  }

  @Override
  public void restoreFromSnapshot(B3251Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

