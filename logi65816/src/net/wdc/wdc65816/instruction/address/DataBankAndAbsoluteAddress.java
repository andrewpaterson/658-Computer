package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.W65C816;

public class DataBankAndAbsoluteAddress
    extends OffsetAddressCycle
{
  public DataBankAndAbsoluteAddress(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(W65C816 cpu)
  {
    return new Address(cpu.getDataBank(), cpu.getAddress().getOffset()).offset(offset, false);
  }
}

