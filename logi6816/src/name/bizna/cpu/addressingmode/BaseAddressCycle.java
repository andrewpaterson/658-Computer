package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Address;
import name.bizna.cpu.Cpu65816;

public abstract class BaseAddressCycle
{
  public abstract Address getAddress(Cpu65816 cpu65816);
}

