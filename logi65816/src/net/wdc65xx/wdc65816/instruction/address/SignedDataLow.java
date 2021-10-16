package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.WDC65C816;

public class SignedDataLow
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65C816 cpu)
  {
    int dataLow = cpu.getDataLow();
    if (cpu.is8bitValueNegative(dataLow))
    {
      return dataLow | 0xffffff00;
    }
    else
    {
      return dataLow;
    }
  }

  @Override
  public String toString()
  {
    return "RL";
  }
}

