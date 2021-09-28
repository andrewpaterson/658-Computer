package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

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

