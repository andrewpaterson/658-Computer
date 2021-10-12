package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class SignedDataLow
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    int dataLow = cpu.getDataLow();
    if (Cpu65816.is8bitValueNegative(dataLow))
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

