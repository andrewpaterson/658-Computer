package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.is16bitValueNegative;

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
