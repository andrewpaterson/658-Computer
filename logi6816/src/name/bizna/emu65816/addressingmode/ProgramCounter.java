package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class ProgramCounterAddress
    extends BaseAddressCycle
{
  public ProgramCounterAddress()
  {
    super();
  }

  @Override
  public Address getAddress(Cpu65816 cpu65816)
  {
    return cpu65816.getProgramCounter();
  }
}

