package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.Cpu65816;

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

