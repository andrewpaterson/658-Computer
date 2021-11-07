package net.integratedcircuits.nexperia.lsf0204;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LSF0204
    extends IntegratedCircuit<LSF0204Snapshot, LSF0204Pins>
{
  public LSF0204(String name, LSF0204Pins pins)
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
          System.out.println("LSF0204.tick: aValue.isUnknown() && !bValue.isUnknown() " + value);
          getPins().setA(value);
        }
        else if (bValue.isUnknown() && !aValue.isUnknown())
        {
          long value = aValue.getValue();
          System.out.println("LSF0204.tick: bValue.isUnknown() && !aValue.isUnknown() " + value);
          getPins().setB(value);
        }
        else if (!bValue.isUnknown() && !aValue.isUnknown())
        {
          long b = bValue.getValue();
          long a = aValue.getValue();
          System.out.println("LSF0204.tick: !bValue.isUnknown() && !aValue.isUnknown() " + a + " " + b);
          getPins().setA(b);
          getPins().setB(a);
        }
      }
    }
  }

  @Override
  public String getType()
  {
    return "4-bit Translator";
  }
}

