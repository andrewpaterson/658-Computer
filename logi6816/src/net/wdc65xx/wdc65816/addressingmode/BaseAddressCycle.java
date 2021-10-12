package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.Cpu65816;

public abstract class BaseAddressCycle
{
  public abstract Address getAddress(Cpu65816 cpu65816);
}

