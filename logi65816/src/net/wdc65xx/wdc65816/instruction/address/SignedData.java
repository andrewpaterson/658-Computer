package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

import static net.wdc65xx.wdc65816.Cpu65816.is16bitValueNegative;

public class SignedData
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    int dataLow = cpu.getData16Bit();
    if (is16bitValueNegative(dataLow))
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
