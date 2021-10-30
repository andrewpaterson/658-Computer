package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.W65C816;

public abstract class BaseAddressCycle
{
  public abstract Address getAddress(W65C816 cpu65816);
}

