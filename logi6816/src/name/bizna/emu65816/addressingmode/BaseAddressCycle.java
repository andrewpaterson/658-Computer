package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public abstract class AddressCycle
{
  public abstract Address getAddress(Cpu65816 cpu65816);
}

