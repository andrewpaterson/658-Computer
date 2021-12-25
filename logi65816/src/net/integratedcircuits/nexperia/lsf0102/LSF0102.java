package net.integratedcircuits.nexperia.lsf0102;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LSF0102
    extends IntegratedCircuit<LSF0102Snapshot, LSF0102Pins>
{
  public static final String TYPE = "2-bit Translator";

  public LSF0102(String name, LSF0102Pins pins)
  {
    super(name, pins);
  }

  @Override
  public void tick()
  {
    BusValue aValue = getPins().getA();
    BusValue bValue = getPins().getB();
    PinValue enableValue = getPins().getEN();

    if (enableValue.isHigh())
    {
      if (!aValue.isError() && !aValue.isNotConnected() &&
          !bValue.isError() && !bValue.isNotConnected())
      {
        if (aValue.isUnknown() && !bValue.isUnknown())
        {
          long value = bValue.getValue();
          getPins().setA(value);
        }
        else if (bValue.isUnknown() && !aValue.isUnknown())
        {
          long value = aValue.getValue();
          getPins().setB(value);
        }
        else if (!bValue.isUnknown() && !aValue.isUnknown())
        {
          long b = bValue.getValue();
          long a = aValue.getValue();
          getPins().setA(b);
          getPins().setB(a);
        }
      }
    }
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

