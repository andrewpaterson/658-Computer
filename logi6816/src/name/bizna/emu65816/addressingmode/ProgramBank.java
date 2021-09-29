package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class DataBank
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return 0;
  }

  @Override
  public int getBank(Cpu65816 cpu)
  {
    return cpu.getDataBank();
  }
}

