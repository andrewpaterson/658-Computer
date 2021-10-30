package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.W65C816;

public class SignedData
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    int dataLow = cpu.getData16Bit();
    if (cpu.is16bitValueNegative(dataLow))
    {
      return dataLow | 0xffff0000;
    }
    else
    {
      return dataLow;
    }
  }

  @Override
  public String toString()
  {
    return "R";
  }
}
