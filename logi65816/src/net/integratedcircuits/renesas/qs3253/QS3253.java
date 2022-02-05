package net.integratedcircuits.renesas.qs3253;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class QS3253
    extends IntegratedCircuit<QS3253Snapshot, QS3253Pins>
{
  public static final String TYPE = "4-to-1 Multiplexer";

  public QS3253(String name, QS3253Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    BusValue selectValue = getPins().getSelect();
    BusValue inputAValue = getPins().getInputA();
    BusValue inputBValue = getPins().getInputB();
    PinValue eAb = getPins().getEAB();
    PinValue eBb = getPins().getEBB();

    if (eAb.isLow())
    {
      if (inputAValue.isValid() && selectValue.isValid())
      {
        long input = inputAValue.getValue();
        long select = selectValue.getValue();
        input = (input >> select) & 1;
        getPins().setYA(input == 1);
      }
      else
      {
        getPins().setYAUnsettled();
      }
    }
    else if (eAb.isHigh())
    {
      getPins().setYAHighImpedance();
    }
    else if (eAb.isNotConnected() || eAb.isUnknown())
    {
      getPins().setYAUnsettled();
    }
    else if (eAb.isError())
    {
      getPins().setYAError();
    }

    if (eBb.isLow())
    {
      if (inputBValue.isValid() && selectValue.isValid())
      {
        long input = inputBValue.getValue();
        long select = selectValue.getValue();
        input = (input >> select) & 1;
        getPins().setYB(input == 1);
      }
      else
      {
        getPins().setYBUnsettled();
      }
    }
    else if (eBb.isHigh())
    {
      getPins().setYBHighImpedance();
    }
    else if (eBb.isNotConnected() || eBb.isUnknown())
    {
      getPins().setYBUnsettled();
    }
    else if (eBb.isError())
    {
      getPins().setYBError();
    }
  }

  @Override
  public QS3253Snapshot createSnapshot()
  {
    return new QS3253Snapshot();
  }

  @Override
  public void restoreFromSnapshot(QS3253Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

