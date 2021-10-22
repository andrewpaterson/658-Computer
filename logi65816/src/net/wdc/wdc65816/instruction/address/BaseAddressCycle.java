package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.WDC65816;

public abstract class BaseAddressCycle
{
  public abstract Address getAddress(WDC65816 cpu65816);
}

