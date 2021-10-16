package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.WDC65C816;

public class AddressCycle
    extends BaseAddressCycle
{
  public AddressCycle(AddressOffset... offsets)
  {
  }

  @Override
  public Address getAddress(WDC65C816 cpu65816)
  {
    return null;
  }
}

