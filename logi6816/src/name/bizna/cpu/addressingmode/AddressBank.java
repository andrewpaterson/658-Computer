package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class AddressBank
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
    return cpu.getAddress().getBank();
  }

  @Override
  public String toString()
  {
    return "AAB,";
  }
}

