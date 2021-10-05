package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.is8bitValueNegative;

public class SignedDataLow
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    int dataLow = cpu.getDataLow();
    if (is8bitValueNegative(dataLow))
    {
      return dataLow | 0xffffff00;
    }
    else
    {
      return dataLow;
    }
  }
}
