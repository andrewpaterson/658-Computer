package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class DirectPageAddress
    extends AddressCycle
{
  protected int offset;

  public DirectPageAddress(int offset)
  {
    this.offset = offset;
  }

  @Override
  public Address getAddress(Cpu65816 cpu65816)
  {
    return null;
  }
}
