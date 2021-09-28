package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class DirectPageAddressPlusY
    extends AddressCycle
{
  private int offset;

  public DirectPageAddressPlusY(int offset)
  {
    super();
    this.offset = offset;
  }

  @Override
  public Address getAddress(Cpu65816 cpu65816)
  {
    return null;
  }
}

