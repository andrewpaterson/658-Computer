package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Address;
import name.bizna.cpu.Cpu65816;

public class DataBankAndAbsoluteAddress
    extends OffsetAddressCycle
{
  public DataBankAndAbsoluteAddress(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(Cpu65816 cpu)
  {
    return new Address(cpu.getDataBank(), cpu.getAddress().getOffset()).offset(offset, false);
  }
}

